


-- Users

INSERT INTO users(name,email,password,picture,address,phone,role) VALUES('admin','admin@email.com','pass','/images/profile.png',null,null,'admin')
INSERT INTO users(name,email,password,picture,address,phone,role) VALUES('owner','owner@email.com','pass','/images/profile.png',null,null,'owner')
INSERT INTO users(name,email,password,picture,address,phone,role) VALUES('bidder','bidder@email.com','pass','/images/profile.png',null,null,'bidder')


INSERT INTO items(name,description,picture,sold) VALUES('item1','description 1','/images/item.png',false)
INSERT INTO items(name,description,picture,sold) VALUES('item2','description 2','/images/item.png',true)
INSERT INTO items(name,description,picture,sold) VALUES('item3','description 3','/images/item.png',false)

