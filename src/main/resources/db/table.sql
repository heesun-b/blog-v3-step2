create table user_tb (
    id int auto_increment primary key, 
    username varchar not null unique,
    password varchar not null,
    email varchar not null unique, 
    created_at timestamp not null
);

create table board_tb (
    id int auto_increment primary key,
    user_id int not null, 
    title varchar(100) not null,
    thumbnail longtext not null, 
    content longtext not null,
    created_at timestamp not null
); 

create table raply_tb(
    id int auto_increment primary key,
    user_id int not null,
    board_id int not null,
    comment varchar(100) not null,
    created_at timestamp not null
);

commit;