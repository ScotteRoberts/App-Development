/*
 * Scott Roberts
 * SID: 011651729
 * CECS 277
 * Email: s.e.roberts0@gmail.com
 * 
 * Mysthouse is a Mystery House game that includes:
 * A map with strange rooms
 * A player
 * A first aid kit
 * Zombies (three for now)
 * And a will to survive
 * 
 * The player is placed on a randomly generated map with
 * 10-20 rooms. Each room is singly connected to another Room.
 * However, there is only one winning (Outside) room and only
 * one connection to that Outside room. Each room has 2-4 connections
 * to another random room. The player must traverse the map
 * to find the Outside without getting bit by the Zombies.
 */

package cecs277.project_4;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Mysthouse extends ARepl
{
	// Map Slots
	final int maxRooms = 21; //Maximum rooms in the house + 1.
	final int maxDoors = 7; //Maximums doors for each room + 1.
	ArrayList<Room> map; //One way graph
	int numOfRooms; //current iterations # of rooms
	Room outside; //End room that does not have any directions. Winning room
	FirstAid box; //Placeable first aid kit
	
	//Player Slots
	Player player; //Player object
	Room playerStart; //Player's starting room
	int startIndex; //Index of player's starting room
	
	//Zombie Slots
	final int maxZombies = 4; //Max number of zombies + 1
	public ArrayList<Zombie> zombies; //Huey, Dooey, and Louie
	
	// Input slots
	final Scanner input = new Scanner(System.in); //Console input
	String choice; // Door choice (1-6)
	int direction; // temp variable used to check the ascii code of variable (choice)
	
	//Random number generator variables
	private long seed;
	private Random rand;
	
	//Random number Generator Setup
	public void randomNumSetup()
	{
		seed = System.currentTimeMillis();
		rand = new Random(seed);
	}
	//Random number
	public int getRandomNum()
	{
		return rand.nextInt();
	}
	
	/*
	 * Inner class: Room
	 * Rooms in the house
	 */
	class Room
	{
		boolean used = false;
		int roomNum;
		Room(int i)
		{
			roomNum = i;
		}
		ArrayList<Room> doors = new ArrayList<Room>(maxDoors);
		public boolean getUsed() {return used;}
		public void setUsed() {used = true;}
		public int getRoomNum() {return roomNum;}
		public Room getDoor(int roomNum){return doors.get(roomNum);}
	}
	/*
	 * Inner class: Player
	 * Main character's properties
	 */
	class Player
	{
		Room currentRoom;
		int maxBites = 2;
		int biteCount = 0;
		ArrayList <Integer> roomsVisited = new ArrayList<Integer>();
		//Constructor
		Player(Room current)
		{
			currentRoom = current;
			roomsVisited.add(0, 0);
			roomsVisited.add(1, currentRoom.getRoomNum());
		}
		//Player opening a door between 1-6
		public void openDoor(int dir)
		{
			if(null != currentRoom.getDoor(dir))
			{
				currentRoom = currentRoom.getDoor(dir);
				roomsVisited.add(currentRoom.getRoomNum());
			}	
			else
				System.out.println("Door Locked.");
		}
		//Assess player's health points
		void checkPlayerHealth()
		{
			//Player health check
			if (biteCount == maxBites)
			{
				System.out.println("YOU HAVE TURNED INTO A ZOMBIE!");
				done = true;
			}
		}
		public Room getCurrentRoom() {return currentRoom;}
		public int getCurrentRoomNum(){return currentRoom.getRoomNum();}
		public void setCurrentRoom(Room c) {currentRoom = c;}
		public int getMaxBites() {return maxBites;}
		public void addBiteLimit() { maxBites++;}
		public int getBiteCount() {return biteCount;}
		public void addBiteCount() {biteCount++;}
		public ArrayList<Integer> getRoomsVisited() {return roomsVisited;}
		
	}
	/*
	 * Inner class: Zombie
	 * Zombie's traits
	 */
	class Zombie
	{
		String zombieName;
		Room currentRoom;
		Zombie(String name, Room room)
		{
			zombieName = name;
			currentRoom = room;
		}
		//Tells you the zombie name
		public String getZombieName() {return zombieName;}
		//Grabs the zombie's current room
		public Room getCurrentRoom() {return currentRoom;}
		//Finds and moves through an open door
		public void openDoor()
		{
			Room nextRoom = null;
			while(nextRoom == null)
			{
				nextRoom = map.get(currentRoom.getRoomNum()).doors.get(randomDoorNum());
				if (nextRoom != null)
				{
					currentRoom = nextRoom;
				}
			}
		}
		
		//Call for bite. 1 = bite, 0 = no bite
		public boolean bite()
		{
			if (getRandomNum() % 2 == 1)
			{
				System.out.println("Player Biten.");
				return true;
			}
			else
			{
				System.out.println("Player Safe.");
				return false;
			}
		}
		//Each zombie, if in the player room, can bite the player
		void checkZombieBite()
		{
				if(player.getCurrentRoom() == currentRoom)
				{
					System.out.println("LOOK OUT! " + zombieName + " is attacking!");
					if(bite())
						player.addBiteCount();
				}
		}
		
		//Checks four conditions to detect zombies relative
		//to the player's current position
		void zombieNoise()
		{
			boolean once = false;
			for(int i = 1; i < maxDoors; i++)
			{
				if (null != currentRoom.doors.get(i) && null != player.getCurrentRoom().doors.get(i))
				{
					//Check if they are in the same room
					if (currentRoom == player.getCurrentRoom() && !once)
					{
						System.out.println("Stay quiet... " + zombieName + " is in your room.");
						once = true;
					}
					//Check if the zombie's current room is one of the player's doors
					if (currentRoom == player.getCurrentRoom().doors.get(i) && !once)
					{
						//System.out.println(zombieName + " is in room " + currentRoom.roomNum + " and the player can enter from room " + player.getCurrentRoomNum());
						System.out.println("Stay quiet... " + zombieName + " is nearby.");
						once = true;
					}
					//Check if the zombie's doors is the player's current room
					if(currentRoom.doors.get(i) == player.getCurrentRoom() && !once)
					{
						//System.out.println("Player is in room " + player.getCurrentRoomNum() + " and the zombie can enter from room " + currentRoom.roomNum);
						System.out.println("Stay quiet... " + zombieName + " is nearby.");
						once = true;
					}
				}
			}
			
			for (int i = 1; i < maxDoors; i++)
			{
				if (null != currentRoom.doors.get(i))
				{
					for (int j = 1; j < maxDoors; j++)
					{
						//Check if Zombie's doors is one of the player's doors
						if (currentRoom.doors.get(i) == player.getCurrentRoom().doors.get(j) && !once)
						{
							System.out.println("Player is in: " + player.getCurrentRoomNum() + " and will go to" + player.getCurrentRoom().doors.get(j).roomNum);
							System.out.println(zombieName + " is in: " + currentRoom.roomNum + " and will go to" + currentRoom.doors.get(i).roomNum);
							System.out.println("Stay quiet... " + zombieName + " could enter your next room.");
							once = true;
						}
					}
				}
			}
			
		}
	}
	/*
	 * Inner class: FirstAid
	 * First Aid object for increasing player's health
	 */
	class FirstAid
	{
		Room currentRoom;
		public FirstAid(Room current){currentRoom = current;}
		public Room getCurrentRoom(){return currentRoom;}
		//Moves the first aid kit to room 0 (out of bounds)
		public void exhaustFirstAid(){currentRoom = map.get(0);}
		//Call when player moves
		void checkFirstAid()
		{
			//First Aid position is the player's position
			if (player.getCurrentRoom() == currentRoom)
			{
				System.out.println("You have found a first aid kit.");
				System.out.println("Bite limit + 1");
				player.addBiteLimit();
				exhaustFirstAid();
			}
		}
	}
	
	//Random Number Functions
	int randomDoorNum() {return rand.nextInt(6) + 1;} //1-6
	int randomMaxRoomNum() {return rand.nextInt(maxRooms - 10) + 11;} //11-21
	int randomRoomNum() {return rand.nextInt(numOfRooms - 1) + 1;} // 1-numOfRooms
	
	//Initialization of rooms and doors(rooms)
	void setMap()
	{
		map = new ArrayList<Room>(maxRooms); //New list of Rooms
		numOfRooms = randomMaxRoomNum(); //this iteration's number of rooms
		//Rooms for the map
		for (int i = 0; i <= numOfRooms; i++)
		{
			map.add(i, new Room(i)); 
		}
		//Set all doors to null
		for (int i = 0; i <= numOfRooms; i++)
		{
			for (int j = 0; j < maxDoors; j++)
			{
				map.get(i).doors.add(j, null);
			}
		}
	}
	
	//Printing out the mapping of the rooms, doors, player's position, outside room,
	//zombie's positions, and first aid kit position.
	void printMap()
	{
		System.out.println("-----------------------------------------\n");
		System.out.println("Game Map:\n");
		for (int i = 1; i < numOfRooms; i++)
		{
			System.out.println("Room " + i + ": ");
			for (int j = 1; j < 7; j++)
			{
				if(null != map.get(i).doors.get(j))
				{
					System.out.print("Door " + j + " leads to room: ");
					System.out.println(map.get(i).doors.get(j).getRoomNum());
				}	
			}
			System.out.println();
		}
		System.out.println("Number of rooms: " + (numOfRooms -1));
		System.out.println("Player's room: " + player.getCurrentRoomNum());
		System.out.println("Outside room: " + outside.getRoomNum());
		//Starting position of each zombie
		for (int i = 1; i < maxZombies; i++)
		{
			System.out.println(zombies.get(i).getZombieName() + " is at room: " + zombies.get(i).getCurrentRoom().getRoomNum());
		}
		System.out.println("First-Aid-Kit room: " + box.getCurrentRoom().getRoomNum());
		System.out.println("-----------------------------------------");
	}
	
	//Print player's room
	void printPRoom()
	{
		System.out.print("You are currently in room ");
		System.out.println(player.currentRoom.getRoomNum());
	}
	
	//Display all open rooms available to choose from.
	void printOpenRooms()
	{
		System.out.println("Which direction would you like to go?");
		//Room directions
		for (int j = 1; j < 7; j++)
		{
			if(null != player.currentRoom.doors.get(j))
			{
				switch(j)
				{
				case 1:
					System.out.println("1. North");
					break;
				case 2:
					System.out.println("2. South");
					break;
				case 3:
					System.out.println("3. East");
					break;
				case 4:
					System.out.println("4. West");
					break;
				case 5:
					System.out.println("5. Up");
					break;
				case 6:
					System.out.println("6. Down");
					break;
				}
			}	
		}
	}
	
	//Prints Player.roomsVisited (all rooms entered)
	void printRoomsVisited()
	{
		System.out.print("Rooms Visited: ");
		for (int i = 1; i < player.getRoomsVisited().size(); i++)
		{
			System.out.print(player.getRoomsVisited().get(i)+ ", ");
			if(i % 10 == 0)
				System.out.println();
		}
		System.out.println();
	}
	
	//Set each room to connect to one other room
	void setFirstDoors()
	{
		int roomIndex = randomRoomNum(); //Finding an index to start at.
		startIndex = roomIndex; //Determines the player's start
		map.get(0).setUsed(); //set zero room to used
		//map.get(startIndex).setUsed(); //set starting room to used
		Room nextRoom = null;
		int roomCount = 1;
		while (roomCount < numOfRooms - 1) // one of the rooms is the outside (no connections)
		{
			nextRoom = map.get(randomRoomNum()); //random next room
			if (!nextRoom.getUsed() && nextRoom.roomNum != roomIndex) //next room cannot be used or itself
			{
				map.get(roomIndex).doors.set(randomDoorNum(), nextRoom);
				map.get(roomIndex).setUsed();
				roomIndex = nextRoom.roomNum;
				roomCount++;
			}
		}
		
		outside = nextRoom; //the last instance of nextRoom will be outside.
		roomIndex = 0;
		map.get(0).doors.add(1, map.get(0)); //outside will lead to itself
		outside.doors.add(1, map.get(0));
	}
	//Mandatory Second Door
	void setSecondDoors()
	{	
		int roomIndex = 1;
		while (roomIndex < numOfRooms) // one of the rooms is the outside (no connections)
		{
			Room thisRoom = map.get(roomIndex); //sequential room choice
			Room nextRoom = map.get(randomRoomNum());
			int doorIndex = randomDoorNum();
			Room door = thisRoom.doors.get(doorIndex);
			
			if (door == null && thisRoom != outside && nextRoom != outside)
			{
				thisRoom.doors.set(doorIndex, nextRoom);
				roomIndex++;
			}
			if(thisRoom == outside)
			{
				roomIndex++;
			}
		}
	}
	// %50 chance of getting a third door
	void setThirdDoors()
	{
		int roomIndex = 1;
		while (roomIndex < numOfRooms) // one of the rooms is the outside (no connections)
		{
			Room thisRoom = map.get(roomIndex); //sequential room choice
			Room nextRoom = map.get(randomRoomNum());
			int doorIndex = randomDoorNum();
			Room door = thisRoom.doors.get(doorIndex);
			
			if (door == null && thisRoom != outside && nextRoom != outside)
			{
				if(randomRoomNum() % 2 == 0) //Difference in chance
					thisRoom.doors.set(doorIndex, nextRoom);
				roomIndex++;
			}
			if(thisRoom == outside)
			{
				roomIndex++;
			}
		}
	}
	// %25 chance of getting a fourth door
	void setFourthDoors()
	{
		int roomIndex = 1;
		while (roomIndex < numOfRooms) // one of the rooms is the outside (no connections)
		{
			Room thisRoom = map.get(roomIndex); //sequential room choice
			Room nextRoom = map.get(randomRoomNum());
			int doorIndex = randomDoorNum();
			Room door = thisRoom.doors.get(doorIndex);
			
			if (door == null && thisRoom != outside && nextRoom != outside)
			{
				if(randomRoomNum() % 4 == 0)  //Difference in chance
					thisRoom.doors.set(doorIndex, nextRoom);
				roomIndex++;
			}
			if(thisRoom == outside)
			{
				roomIndex++;
			}
		}
	}
	//Inserts the first aid kit into a random room that is not the player's
	//starting position or the outside.
	void setFirstAid()
	{
		Room boxRoom;
		int boxStart;
		do
		{
			boxStart = randomRoomNum();
			boxRoom = map.get(boxStart);
		}
		while(boxStart == outside.getRoomNum() || boxStart == player.getCurrentRoomNum());
		box = new FirstAid(boxRoom);
	}
	
	//Huey, Dooey, and Louie starting position that is not the player's
	//current room or the outside.
	void setZombies()
	{
		zombies = new ArrayList<Zombie>(maxZombies);
		zombies.add(0, null);
		
		int zombieStart;
	
		//Zombie Huey
		do{
			zombieStart = randomRoomNum();
		} while(zombieStart == player.getCurrentRoomNum() || zombieStart == outside.getRoomNum());
		zombies.add(1, new Zombie("Huey", map.get(zombieStart)));
		
		//Zombie Dooey
		do{
			zombieStart = randomRoomNum();
		} while(zombieStart == player.getCurrentRoomNum() || zombieStart == outside.getRoomNum());
		zombies.add(2, new Zombie("Dooey", map.get(zombieStart)));
		
		//Zombie Louie
		do{
			zombieStart = randomRoomNum();
		} while(zombieStart == player.getCurrentRoomNum() || zombieStart == outside.getRoomNum());
		zombies.add(3, new Zombie("Louie", map.get(zombieStart)));
	}
	//Sets player at a start position determined by setFirstDoors()
	void setPlayer()
	{
		player = new Player(map.get(startIndex));
	}
	//Input validation for door choice (1-6)
	void validateInt()
	{
		do{
			System.out.print("\nPlease pick a number: ");
			choice = input.next();
			direction = (int) choice.charAt(0);
		} while(direction < 49 || direction > 54);
		direction -= 48;
	}
	
	@Override
	public void setup()
	{
		randomNumSetup();
		setMap();
		setFirstDoors();
		setSecondDoors();
		setThirdDoors();
		setFourthDoors();
		setPlayer();
		setFirstAid();
		setZombies();
		printMap();
	}

	@Override
	public void hello() 
	{
		System.out.println("\n*********************");
		System.out.println("**  Mystery House  **");
		System.out.println("*********************\n");
		System.out.println("THE ZOMBIES ARE COMING!!");
		System.out.println("GET TO THE OUTSIDE ALIVE!\n");
		System.out.println("To pick a direction, enter the number");
		System.out.println("the direction represents.\n");
		System.out.println("Hint: use the map above if");
		System.out.println("you get lost.");
		System.out.println("-----------------------------------------\n");
	}

	@Override
	public void listen() {
		for(int i = 1; i< maxZombies; i++)
			zombies.get(i).zombieNoise();
		System.out.println();
		printRoomsVisited();
		printPRoom();
		printOpenRooms();
		validateInt();
		System.out.println();
	}

	@Override
	public void respond() {
		player.openDoor(direction);
		
		box.checkFirstAid();
		
		for(int i = 1; i< maxZombies; i++)
			zombies.get(i).checkZombieBite();
		
		player.checkPlayerHealth();
		
		System.out.println("Zombies are moving...");
		for(int i = 1; i< maxZombies; i++)
			zombies.get(i).openDoor();
		
		for(int i = 1; i< maxZombies; i++)
			zombies.get(i).checkZombieBite();
		
		player.checkPlayerHealth();
		System.out.println("-----------------------------------------");
	}

	@Override
	public boolean endCheck() {
		if(player.getCurrentRoom() == outside)
		{
			done = true;
		}
		return done;
	}

	@Override
	public void cleanup()
	{
		if(player.getBiteCount() < player.getMaxBites())
		{
			System.out.println("You made it outside!");
			System.out.println("Is it truly over?...");
		}
	}
	
	public static void main(String[] args) {
		Mysthouse mObj = new Mysthouse();
		mObj.repl();
	}

}
