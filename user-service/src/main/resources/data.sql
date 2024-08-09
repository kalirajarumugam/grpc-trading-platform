DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS portfolio_item;

-- intentionally naming this table as customer as "user" has some issues
CREATE TABLE customer (
    id int primary key,
    name VARCHAR(50),
    balance int
);

CREATE TABLE portfolio_item (
    id int AUTO_INCREMENT primary key,
    customer_id int,
    ticker VARCHAR(10),
    quantity int,
    foreign key (customer_id) references customer(id)
);

insert into customer(id, name, balance)
    values
        (1, 'Sam', 10000),
        (2, 'Mike', 10000),
        (3, 'John', 10000);

