
    create table book (
        price float(53),
        book_id bigint not null auto_increment,
        author varchar(150),
        description TEXT,
        title varchar(255) not null,
        primary key (book_id)
    ) engine=InnoDB;

    create table book_copy (
        book_id bigint not null,
        copy_id bigint not null auto_increment,
        entry_date datetime(6),
        barcode varchar(255) not null,
        shelf_location varchar(255),
        status varchar(255),
        primary key (copy_id)
    ) engine=InnoDB;

    create table book_review (
        rating integer not null,
        book_id bigint not null,
        created_at datetime(6),
        review_id bigint not null auto_increment,
        student_id bigint not null,
        comment TEXT,
        primary key (review_id)
    ) engine=InnoDB;

    create table category (
        book_id bigint not null,
        category_id bigint not null auto_increment,
        category_name varchar(100) not null,
        description TEXT,
        primary key (category_id)
    ) engine=InnoDB;

    create table fine_payment (
        amount_paid float(53) not null,
        payment_date datetime(6),
        payment_id bigint not null auto_increment,
        violation_id bigint,
        payment_method varchar(255),
        primary key (payment_id)
    ) engine=InnoDB;

    create table incident (
        created_at datetime(6),
        incident_id bigint not null auto_increment,
        staff_id bigint,
        user_id bigint not null,
        priority varchar(20),
        status varchar(50),
        title varchar(200) not null,
        description TEXT,
        primary key (incident_id)
    ) engine=InnoDB;

    create table loan (
        borrow_date datetime(6) not null,
        copy_id bigint not null,
        due_date datetime(6) not null,
        loan_id bigint not null auto_increment,
        returned_at datetime(6),
        staff_id bigint,
        user_id bigint not null,
        status varchar(255),
        primary key (loan_id)
    ) engine=InnoDB;

    create table notification (
        is_read bit,
        created_at datetime(6),
        notification_id bigint not null auto_increment,
        user_id bigint not null,
        content varchar(255),
        title varchar(255),
        type varchar(255) not null,
        primary key (notification_id)
    ) engine=InnoDB;

    create table permission (
        id bigint not null auto_increment,
        description varchar(255),
        module varchar(255) not null,
        permission_name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table reservation (
        book_id bigint not null,
        copy_id bigint,
        expiration_date datetime(6),
        loan_id bigint,
        request_date datetime(6) not null,
        reservation_id bigint not null auto_increment,
        student_id bigint not null,
        status varchar(50),
        primary key (reservation_id)
    ) engine=InnoDB;

    create table return_transaction (
        late_fee float(53),
        loan_id bigint not null,
        return_date datetime(6) not null,
        return_id bigint not null auto_increment,
        staff_id bigint,
        actual_condition varchar(255),
        primary key (return_id)
    ) engine=InnoDB;

    create table role (
        id bigint not null auto_increment,
        role_name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table role_permission (
        permission_id bigint not null,
        role_id bigint not null,
        primary key (permission_id, role_id)
    ) engine=InnoDB;

    create table shelf (
        floor_level integer,
        book_id bigint not null,
        shelf_id bigint not null auto_increment,
        area_zone varchar(50),
        shelf_code varchar(50) not null,
        primary key (shelf_id)
    ) engine=InnoDB;

    create table staff (
        staff_id bigint not null,
        position varchar(255),
        primary key (staff_id)
    ) engine=InnoDB;

    create table student (
        total_debt float(53),
        student_id bigint not null,
        class varchar(255) not null,
        major varchar(255),
        primary key (student_id)
    ) engine=InnoDB;

    create table system_rule (
        rule_id bigint not null auto_increment,
        data_type varchar(20),
        unit varchar(50),
        rule_key varchar(100) not null,
        description TEXT,
        rule_value varchar(255) not null,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
        user_id bigint not null auto_increment,
        user_name varchar(50) not null,
        email varchar(255) not null,
        full_name varchar(255) not null,
        password varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    create table user_role (
        role_id bigint not null,
        user_id bigint not null,
        primary key (role_id, user_id)
    ) engine=InnoDB;

    create table violation (
        fine_amount float(53),
        is_paid bit,
        loan_id bigint,
        violation_id bigint not null auto_increment,
        type varchar(255),
        primary key (violation_id)
    ) engine=InnoDB;

    alter table book_copy 
       add constraint UK2ljaasl2jjo2yh2i05s2dqcgv unique (barcode);

    alter table category 
       add constraint UKlroeo5fvfdeg4hpicn4lw7x9b unique (category_name);

    alter table permission 
       add constraint UKqaohn9ltifigvuyokkibp6it7 unique (module);

    alter table permission 
       add constraint UKl3pmqryh8vgle52647itattb9 unique (permission_name);

    alter table reservation 
       add constraint UKtgvb269agytbg2eo5e1ptin22 unique (loan_id);

    alter table return_transaction 
       add constraint UKkcotoatdaetxjl66m29mih3nl unique (loan_id);

    alter table role 
       add constraint UKiubw515ff0ugtm28p8g3myt0h unique (role_name);

    alter table shelf 
       add constraint UKdiheshwvt1uicyni1gmetnygv unique (shelf_code);

    alter table system_rule 
       add constraint UK97a15n5xii0kwbv3xmaa26rby unique (rule_key);

    alter table user 
       add constraint UKlqjrcobrh9jc8wpcar64q1bfh unique (user_name);

    alter table user 
       add constraint UKob8kqyqqgmefl0aco34akdtpe unique (email);

    alter table book_copy 
       add constraint FKpqftp65hd66ae8wsx7pp2cxcs 
       foreign key (book_id) 
       references book (book_id);

    alter table book_review 
       add constraint FK29oatdl4f30mtg65oxo1nkmjg 
       foreign key (book_id) 
       references book (book_id);

    alter table book_review 
       add constraint FKr4s98jfm6nyssquabvtyunt9k 
       foreign key (student_id) 
       references student (student_id);

    alter table category 
       add constraint FKap0cnk1255oj4bwam7in1hxxv 
       foreign key (category_id) 
       references category (category_id);

    alter table category 
       add constraint FKgf90y39kxxy7de579c3hvbu2 
       foreign key (book_id) 
       references book (book_id);

    alter table fine_payment 
       add constraint FKin5tmwq189vw0qmibvcn9etx8 
       foreign key (violation_id) 
       references violation (violation_id);

    alter table incident 
       add constraint FK21sxrf6te3dalxt3r695xpnao 
       foreign key (staff_id) 
       references staff (staff_id);

    alter table incident 
       add constraint FK8bqewpr8w6nc8leoue11rmuew 
       foreign key (user_id) 
       references user (user_id);

    alter table loan 
       add constraint FKb9o1lncxf08drw1txeapk3n8j 
       foreign key (copy_id) 
       references book_copy (copy_id);

    alter table loan 
       add constraint FKng56xwv795o80bivbtwj4ljep 
       foreign key (staff_id) 
       references staff (staff_id);

    alter table loan 
       add constraint FKxxm1yc1xty3qn1pthgj8ac4f 
       foreign key (user_id) 
       references user (user_id);

    alter table notification 
       add constraint FKb0yvoep4h4k92ipon31wmdf7e 
       foreign key (user_id) 
       references user (user_id);

    alter table reservation 
       add constraint FKirxtcw4s6lhwi6l9ocrk6bjfy 
       foreign key (book_id) 
       references book (book_id);

    alter table reservation 
       add constraint FKgwfxiuu09uu12x30v8nyclrg4 
       foreign key (copy_id) 
       references book_copy (copy_id);

    alter table reservation 
       add constraint FK6si0rihkp5oe0nte68ol3d66u 
       foreign key (loan_id) 
       references loan (loan_id);

    alter table reservation 
       add constraint FKiuft3416ayrn5538t6bd108fw 
       foreign key (student_id) 
       references student (student_id);

    alter table return_transaction 
       add constraint FKc0h2rgno2k2b0og33kx57x9gx 
       foreign key (loan_id) 
       references loan (loan_id);

    alter table return_transaction 
       add constraint FKfoeon58tw3lesatqirbbummme 
       foreign key (staff_id) 
       references staff (staff_id);

    alter table role_permission 
       add constraint FKf8yllw1ecvwqy3ehyxawqa1qp 
       foreign key (permission_id) 
       references permission (id);

    alter table role_permission 
       add constraint FKa6jx8n8xkesmjmv6jqug6bg68 
       foreign key (role_id) 
       references role (id);

    alter table shelf 
       add constraint FK68q60ybewbn4yko3jh1ev916g 
       foreign key (shelf_id) 
       references shelf (shelf_id);

    alter table shelf 
       add constraint FKke1nix3mcw7vyk485wgg9bvo8 
       foreign key (book_id) 
       references book (book_id);

    alter table staff 
       add constraint FKdjc9hhxhn1bbg3j6y31dgw31k 
       foreign key (staff_id) 
       references user (user_id);

    alter table student 
       add constraint FKmlyxb44mx4v92t1eq5v655fk3 
       foreign key (student_id) 
       references user (user_id);

    alter table user_role 
       add constraint FKa68196081fvovjhkek5m97n3y 
       foreign key (role_id) 
       references role (id);

    alter table user_role 
       add constraint FK859n2jvi8ivhui0rl0esws6o 
       foreign key (user_id) 
       references user (user_id);

    alter table violation 
       add constraint FKejc9hvi5i8a6mn77hwsmda9g1 
       foreign key (loan_id) 
       references loan (loan_id);
