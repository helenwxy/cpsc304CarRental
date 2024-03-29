create table customer
(
    dlicense varchar(20) not null PRIMARY KEY,
    name     varchar(20) not null,
    address  varchar(40) not null,
    phone    varchar(20) not null unique
);

create table vehicleType
(
    vtname  varchar(20) not null PRIMARY KEY,
    feature varchar(20) not null,
    wrate   float       not null,
    drate   float       not null,
    hrate   float       not null,
    wirate  float       not null,
    dirate  float       not null,
    hirate  float       not null,
    krate   float       not null
);

create table vehicle
(
    vid      integer     not null,
    vlicense varchar(20) not null PRIMARY KEY,
    make     varchar(20) not null,
    model    varchar(20) not null,
    year     integer     not null,
    odometer integer     not null,
    status   varchar(20) not null,
    vtname               not null,
    location varchar(40) not null,
    city     varchar(20) not null,
    foreign key (vtname) references vehicleType ON DELETE CASCADE,
    CHECK (status IN ('Available', 'Rented', 'Maintenance'))
);

create table reservation
(
    confNo   integer     not null PRIMARY KEY,
    vtname   varchar(20) not null,
    dlicense varchar(20) not null,
    fromDate date        not null,
    toDate   date        not null,
    rDate    date        not null,
    foreign key (vtname) references vehicleType ON DELETE CASCADE,
    foreign key (dlicense) references customer ON DELETE CASCADE
);

create table rental
(
    rid      integer     not null PRIMARY KEY,
    vlicense varchar(20) not null,
    dlicense varchar(20) not null,
    fromDate date        not null,
    toDate   date        not null,
    odometer integer     not null,
    cardName varchar(20) not null,
    cardNo   varchar(20) not null,
    expDate  date        not null,
    confNo   integer unique,
    foreign key (vlicense) references vehicle ON DELETE CASCADE,
    foreign key (dlicense) references customer ON DELETE CASCADE,
    foreign key (confNo) references reservation ON DELETE CASCADE,
    CHECK (cardName IN ('Mastercard', 'Visa'))
);

create table return
(
    rid      integer not null PRIMARY KEY,
    rdate    date    not null,
    odometer integer not null,
    fulltank number  not null,
    value    float   not null,
    foreign key (rid) references rental ON DELETE CASCADE
);