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

CREATE TABLE Game(
game_id INTEGER NOT NULL AUTO_INCREMENT,
user_id1 INTEGER,
user_id2 INTEGER,
result INTEGER,
time INTEGER NOT NULL,
increment INTEGER NOT NULL,
opponent INTEGER,
CONSTRAINT game_pk PRIMARY KEY (game_id)
);
ALTER TABLE Game AUTO_INCREMENT = 100000;

ALTER TABLE Game
ADD CONSTRAINT game_pk1 FOREIGN KEY(user_id1)
REFERENCES User(user_id);

ALTER TABLE Game
ADD CONSTRAINT game_fk2 FOREIGN KEY(user_id2)
REFERENCES User(user_id);

ALTER TABLE Game
ADD CONSTRAINT game_fk3 FOREIGN KEY(opponent)
REFERENCES User(user_id);



CREATE TABLE Move(
game_id INTEGER NOT NULL,
movenr INTEGER NOT NULL,
fromX INTEGER NOT NULL,
fromY INTEGER NOT NULL,
toX INTEGER NOT NULL,
toY INTEGER NOT NULL,
CONSTRAINT move_pk PRIMARY KEY(game_id, movenr)
);

ALTER TABLE Move
ADD CONSTRAINT move_pk FOREIGN KEY(game_id)
REFERENCES Game(game_id);

DROP TABLE Chat
CREATE TABLE Chat(
msg_id INTEGER AUTO_INCREMENT,
gameid INTEGER NOT NULL,
msg VARCHAR(240),
user_id INTEGER NOT NULL,
CONSTRAINT chat_pk PRIMARY KEY(msg_id, gameid)
)

ALTER TABLE Chat
ADD CONSTRAINT chat_fk FOREIGN KEY(gameid)
REFERENCES Game(game_id);

ALTER TABLE Chat
ADD CONSTRAINT chat_fk2 FOREIGN KEY(user_id)
REFERENCES User(user_id);

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

-- For creating the userElo table NOT TESTED
Create table userElo (
        userID int NOT NULL,
        game int NOT NULL AUTO_INCREMENT,
        elo int,
        CONSTRAINT pk PRIMARY KEY (game, userID),
        FOREIGN KEY (userID) REFERENCES User(user_id)
);

ALTER TABLE Game
ADD mode INTEGER NOT NULL;

SELECT * FROM Game WHERE user_id1 IS NOT NULL  AND user_id2 IS NOT NULL AND game_id = ;

SELECT * FROM Move WHERE game_id = 100275;

SELECT COUNT(game_id) FROM Game WHERE user_id1 = 1 OR user_id2 = 1;

SELECT COUNT(game_id) FROM Game WHERE result = 6;

UPDATE Game SET active = 0 WHERE active = 1;

SELECT * FROM UserSettings;

SELECT * FROM User;

SELECT *FROM Game WHERE (user_id1 = 6 OR user_id2 = 6) AND result IS NOT NULL ;

SELECT COUNT(game_id) FROM Game WHERE result = 7;

UPDATE UserSettings SET skinName = 'Pink' WHERE username = 'EpicHaxor';

UPDATE Game SET result = 7 WHERE user_id2 = 6;

SELECT *FROM Move WHERE game_id = 100394;

ALTER TABLE Game
ALTER result SET DEFAULT -1;

UPDATE User SET ELOrating
= 1200 WHERE user_id = 7;

SELECT user_id FROM User WHERE username = "Test";

SET GLOBAL time_zone = '+1:00';

SELECT * FROM Game where  game_id > 100501;

SELECT * FROM userElo WHERE game > 500;

SELECT * FROM User;

UPDATE User SET ELOrating = 2000 WHERE username = "dillyboi";

