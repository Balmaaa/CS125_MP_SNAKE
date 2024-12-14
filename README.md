
# Snake (Multiplayer) 🐍
A step beyond reviving old-school classics. Snake is an old-school computer game with a premise of a snake controlled by the user with a goal of consuming as many apples as possible. Now, this is a modern take on snake with a twist... MULTIPLAYER GAMEPLAY!

## Synchronization & Threading in Snake Multiplayer
Old versions of Snake uses sequential where the flow is sequenced as:

        Snake -> Apple -> Register Score -> Allow Snake to Move

However, this modern take on Snake utilizes synchronization to allow two snakes/ multiple threads to move simultaneously whilst having all other elements occur and function alongside each other.

## Project Flow
1. Drafting of Snake Game Design and Flow
2. Developed foundation and base layout
3. Improved implementation of synchronization
4. Added UI changes and modifications
5. Refined overall project

## ⚙️ Prerequisites
- Visual Studio Code / IntelliJ (Any IDE will work as well)
- Java Development Kit (JDK) Version 21 or 23 (Preferably Ver. 23)
- Git (To Clone GitHub Repository)
- Cloned Version of GitHub Repository

## 🎮 How To Run?
1. Clone GitHub Repository

        git clone [repository-url]
2. Open the cloned repository with the IDE of your choice
3. Locate the file GameMenu.java

        cd .../CS125_MP_SNAKE/src/GameMenu.java

4. For Visual Studio Code. Press the Start Button at the Upper Right
5. Alternative Solution to Run Game.

        javac [filename]
        java [filename]

        Example:
        javac snakegame.java
        java snakegame
    


## 👥 Contact Us
    Balmaaa     | gsbalmaceda@up.edu.ph
    ljdvr       | lddevera@up.edu.ph
    JuyeonCodez | lglumain@up.edu.ph   

