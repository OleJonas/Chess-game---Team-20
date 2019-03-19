-- For creating the User table
Create table User(
                   user_id INT AUTO_INCREMENT,
                   username VARCHAR(20),
                   avatar VARCHAR(15),
                   gamesPlayed int not null,
                   gamesWon int not null,
                   gamesLost int not null,
                   gamesRemis int not null,
                   ELOrating int not null,
                   password VARCHAR(250),
                   SALT VARCHAR(250),
                   PRIMARY KEY (User_id)
);

-- For creating the UserSettings table
Create table UserSettings (
                  user_id int not null AUTO_INCREMENT,
                  username VARCHAR(30),
                  darkTileColor VARCHAR(10),
                  lightTileColor VARCHAR(10),
                  skinName VARCHAR(30),
                  PRIMARY KEY (user_id),
                  FOREIGN KEY (user_id) REFERENCES User(user_id)
);

