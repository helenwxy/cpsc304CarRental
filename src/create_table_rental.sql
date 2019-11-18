create table customer (
    dlicense varchar(20) not null PRIMARY KEY,
    name varchar(20) not null,
    address varchar(20) not null,
    phone integer
);

create table vehicleType (
    vtname varchar(20) not null PRIMARY KEY,
    feature varchar(20) not null,
    wrate float not null,
    drate float not null,
    hrate float not null,
    wirate float not null,
    dirate float not null,
    hirate float not null,
    krate float not null
);

create table vehicle (
    vlicense varchar(20) not null PRIMARY KEY,
    vid integer not null PRIMARY KEY,
    make varchar(20) not null,
    model varchar(20) not null,
    year integer not null,
    odometer integer not null,
    status varchar(20) not null,
    vtname not null,
    location varchar(20) not null,
    city varchar(20) not null,
    foreign key (vtname) references vehicleType,
    CHECK (status IN ('Available', 'Rented', 'Maintenance'))
);

create table reservation (
    confNo integer not null PRIMARY KEY,
    vtname varchar(20) not null,
    dlicense varchar(20) not null,
    fromDate date not null,
    toDate date not null,
    fromTime date not null,
    toTime date not null,
    foreign key (vtname) references vehicleType,
    foreign key (dlicense) references customer
);

create table rental (
    rid integer not null PRIMARY KEY,
    vlicense integer not null,
    dlicense varchar(20) not null,
    fromDate date not null,
    toDate date not null,
    fromTime date not null,
    toTime date not null,
    odometer integer not null,
    cardName varchar(20) not null,
    cardNo integer not null,
    expDate date not null,
    confNo integer,
    foreign key (vlicense) references vehicle,
    foreign key (dlicense) references customer,
    foreign key (confNo) references reservation
);

create table return (
    rid integer not null PRIMARY KEY,
    rdate date not null,
    rtime date not null,
    odometer integer not null,
    fulltank number not null,
    value float not null,
    foreign key (rid) references rental
);