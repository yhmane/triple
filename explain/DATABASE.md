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
```