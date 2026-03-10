// db.auth('username', 'password');

db = db.getSiblingDB('mongo_db');

db.createUser(
    {
        user: "username",
        pwd: "password",
        roles: [{role: "readWrite", db: "mongo_db"}]
    }
);

db.createCollection("mongo_news");

db = db.getSiblingDB('graphql_db');

db.createUser(
    {
        user: "username",
        pwd: "password",
        roles: [{role: "readWrite", db: "graphql_db"}]
    }
);

db.createCollection("mongo_book");
