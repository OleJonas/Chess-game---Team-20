-- For creating the user database
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