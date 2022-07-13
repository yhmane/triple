## table
```sql
create table point (
    point_id bigint not null auto_increment,
    points bigint,
    user_id varchar(255),
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    primary key (point_id)
) engine=InnoDB;

create table point_history (
    history_id bigint not null auto_increment,
    user_id varchar(255),
    review_id varchar(255),
    points bigint not null,
    action_type varchar(255),
    increase_type varchar(255),
    created_at datetime(6) not null, 
    updated_at datetime(6) not null, 
    primary key (history_id)
) engine=InnoDB;

create table review (
    review_id varchar(255) not null,
    user_id varchar(255),
    place_id varchar(255),
    content varchar(255),
    created_at datetime(6) not null, 
    updated_at datetime(6) not null,  
    primary key (review_id)
) engine=InnoDB;

create table review_photo (
    attached_photo_id varchar(255) not null,
    review_id varchar(255), 
    created_at datetime(6) not null, 
    updated_at datetime(6) not null, 
    primary key (attached_photo_id)
) engine=InnoDB;

alter table review_photo add constraint FK80ti8nek4uv8vn4vjhpre6mwg foreign key (review_id) references review (review_id)
```

## index
```sql
alter table point add constraint point_index_1 unique (user_id);
create index point_history_index_1 on point_history (review_id, point_type);
create index review_index_1 on review (place_id, created_at);
```