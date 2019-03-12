-- For creating the user database
Create table User(
                   user_id INT AUTO_INCREMENT,
                   username VARCHAR(20),
                   password VARCHAR(250),
                   SALT VARCHAR(250),
                   avatar VARCHAR(15),
                   PRIMARY KEY (User_id)
);