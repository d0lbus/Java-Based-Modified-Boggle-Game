# IT 222L Integrative Technologies – Final Project

## Project Overview
This project is a **multiplayer client-server word game (“Boggled”)** built using **CORBA in Java** with **MySQL integration**.  
It demonstrates distributed computing concepts, database integration, and GUI-based client-server interaction.

## Features
- **Client-Server System**: Players log in with verified credentials; only one active session per account.  
- **Gameplay**:  
  - Server sends 20 random letters (13 consonants, 7 vowels).  
  - Players form valid words (minimum 4 letters) within 30 seconds.  
  - Unique words earn scores, and the first to win 3 rounds is declared the winner.  
- **Administrator Functions**:  
  - CRUD + search operations for player accounts.  
  - Configure waiting time and round duration.  
  - Access leaderboard showing top 5 scores.  
- **Database Integration**: All game data stored in MySQL.  
- **Word Validation**: Uses `words.txt` for dictionary checking.  

## How to Run
1. Set up MySQL database and import required schema.  
2. Compile and run the CORBA server in Java.  
3. Connect client(s) using the provided GUI.  
4. Use administrator functions for account and game management.  

---
