insert into customer values ('GA123456', 'Bob Smith', '111 E. 11 St., Vancouver', 6043121234);
insert into customer values ('GC222222', 'Candace Walters', '222 E. 22 St., Burnaby', 6049875432);
insert into customer values ('BA112345', 'Kara Rops', '333 W. 33 Ave., Richmond', 7781234444);
insert into customer values ('AC123145', 'Alex Mark', '444 E. 4 Ave., Vancouver', 6041111111);
insert into customer values ('EE123111', 'John Smith', '414 E. 26 Ave., Vancouver', 6049898989);

insert into vehicleType values ('Economy', 'Auto lock', 300, 50, 10, 200, 40, 5, 2);
insert into vehicleType values ('Compact', 'AC', 200, 30, 5, 200, 40, 5, 2);
insert into vehicleType values ('Mid-size', 'Heated seat', 350, 55, 12, 200, 40, 5, 2);
insert into vehicleType values ('Standard', 'Heated seat', 400, 60, 15, 200, 40, 5, 2);
insert into vehicleType values ('Full-size', 'Leather seat', 450, 65, 20, 300, 50, 10, 2.5);
insert into vehicleType values ('SUV', 'Big trunk size', 500, 70, 25, 350, 60, 15, 3);
insert into vehicleType values ('Truck', 'Cargo space', 550, 80, 30, 400, 70, 20, 3.5);

insert into vehicle (1, '111AAA', 'Honda', 'Civic', 2001, 11111, 'Rented', 'Compact', '101 E. 11 St., Vancouver', 'Vancouver');
insert into vehicle (2, '222BBB', 'Toyota', 'Corolla', 2002, 22222, 'Available', 'Mid-size', '202 W. 22 St., Surrey', 'Surrey');
insert into vehicle (3, '333CCC', 'Hyundai', 'Elantra', 2003, 33333, 'Maintenance', 'Compact', '303 E. 33 St., Richmond', 'Richmond');
insert into vehicle (4, '444DDD', 'Mazda', 'Maxda3', 2004, 44444, 'Rented', 'Compact', '404 W. 44 St., Burnaby', 'Burnaby');
insert into vehicle (5, '555EEE', 'Mercedes-Benz', 'G-Class', 2005, 55555, 'Available', 'SUV', '505 E. 55 St., Coquitlam', 'Coquitlam');
insert into vehicle (6, '666FFF', 'Ford', 'Mondeo', 2006, 66666, 'Maintenance', 'Standard', '606 W. 66 St., Vancouver', 'Vancouver');
insert into vehicle (7, '777GGG', 'Dodge', 'Charger', 2007, 77777, 'Rented', 'Fill-size', '707 E. 77 St., Richmond', 'Richmond');
insert into vehicle (8, '888HHH', 'Jeep', 'Gladiator', 2008, 88888, 'Available', 'Truck', '808 W. 88 St., Coquitlam', 'Coquitlam');
insert into vehicle (9, '999III', 'Nissan', 'Rogue', 2009, 99999, 'Maintenance', 'SUV', '909 E. 99 St., Surrey', 'Surrey');

insert into reservation (1, 'Economy', 'GA123456', TO_DATE('01-JAN-2001','DD-MM-YYYY'), TO_DATE('01-MAR-2001','DD-MM-YYYY'), TO_DATE('08:20 AM', 'HH:MI AM'), TO_DATE('04:40 PM', 'HH:MI PM'));
insert into reservation (2, 'Compact', 'GA123456', TO_DATE('02-APR-2001', 'DD-MM-YYYY'), TO_DATE('02-APR-2001', 'DD-MM-YYYY'), TO_DATE('10:10 AM', 'HH:MI AM'), TO_DATE('11:40 AM', 'HH:MI AM'));
insert into reservation (3, 'Compact', 'GC222222', TO_DATE('03-APR-2003', 'DD-MM-YYYY'), TO_DATE('31-DEC-2003', 'DD-MM-YYYY'), TO_DATE('00:00 AM', 'HH:MI AM'), TO_DATE('11:59 PM', 'HH:MI PM'));
insert into reservation (4, 'Mid-size', 'BA112345', TO_DATE('14-AUG-2014', 'DD-MM-YYYY'), TO_DATE('17-AUG-2014', 'DD-MM-YYYY'), TO_DATE('03:00 PM', 'HH:MI PM'), TO_DATE('07:30 PM', 'HH:MI PM'));
insert into reservation (5, 'Standard', 'AC123145', TO_DATE('25-MAY-2005', 'DD-MM-YYYY'), TO_DATE('29-JUL-2005', 'DD-MM-YYYY'), TO_DATE('05:00 AM', 'HH:MI AM'), TO_DATE('01:00 PM', 'HH:MI PM'));
insert into reservation (6, 'Full-size', 'EE123111', TO_DATE('16-NOV-2003', 'DD-MM-YYYY'), TO_DATE('06-JAN-2004', 'DD-MM-YYYY'), TO_DATE('06:06 AM', 'HH:MI AM'), TO_DATE('06:16 PM', 'HH:MI PM'));
insert into reservation (7, 'SUV', 'EE123111', TO_DATE('04-FEB-2004', 'DD-MM-YYYY'), TO_DATE('15-JUN-2003', 'DD-MM-YYYY'), TO_DATE('04:52 AM', 'HH:MI AM'), TO_DATE('00:23 PM', 'HH:MI PM'));
insert into reservation (8, 'Standard', 'EE123111', TO_DATE('08-AUG-2008', 'DD-MM-YYYY'), TO_DATE('09-SEP-2009', 'DD-MM-YYYY'), TO_DATE('08:48 AM', 'HH:MI AM'), TO_DATE('09:39 PM', 'HH:MI PM'));
insert into reservation (9, 'Full-size', 'EE123111', TO_DATE('09-SEP-2009', 'DD-MM-YYYY'), TO_DATE('09-SEP-2009', 'DD-MM-YYYY'), TO_DATE('09:40 PM', 'HH:MI PM'), TO_DATE('10:40 PM', 'HH:MI PM'));

insert into rental(1, 1, 'GA123456',
                   TO_DATE('01-JAN-2001','DD-MM-YYYY'), TO_DATE('01-MAR-2001','DD-MM-YYYY'), TO_DATE('08:20 AM', 'HH:MI AM'), TO_DATE('04:40 PM', 'HH:MI PM')
                   5,
                   'Mastercard', 5000123412341234, TO_DATE('01-2020', 'MM-YYYY'),
                   5001);
insert into rental(2, 2, 'GA123456',
                   TO_DATE('02-APR-2001', 'DD-MM-YYYY'), TO_DATE('02-APR-2001', 'DD-MM-YYY'), TO_DATE('10:10 AM', 'HH:MI AM'), TO_DATE('11:40 AM', 'HH:MI AM')
                   10,
                   'Mastercard', 5000234523452345, TO_DATE('02-2020', 'MM-YYYY'),
                   5002);
insert into rental(3, 3, 'GC222222',
                   TO_DATE('03-APR-2003', 'DD-MM-YYYY'), TO_DATE('31-DEC-2003', 'DD-MM-YYYY'), TO_DATE('00:00 AM', 'HH:MI AM'), TO_DATE('11:59 PM', 'HH:MI PM')
                   15,
                   'Mastercard', 5000345634563456, TO_DATE('03-2020', 'MM-YYYY'),
                   5003);
insert into rental(4, 4, 'BA112345',
                   TO_DATE('14-AUG-2014', 'DD-MM-YYYY'), TO_DATE('17-AUG-2014', 'DD-MM-YYYY'), TO_DATE('03:00 PM', 'HH:MI PM'), TO_DATE('07:30 PM', 'HH:MI PM')
                   20,
                   'Mastercard', 5000456745674567, TO_DATE('04-2020', 'MM-YYYY'),
                   4004);
insert into rental(5, 5, 'AC123145',
                   TO_DATE('25-MAY-2005', 'DD-MM-YYYY'), TO_DATE('29-JUL-2005', 'DD-MM-YYYY'), TO_DATE('05:00 AM', 'HH:MI AM'), TO_DATE('01:00 PM', 'HH:MI PM')
                   25, 'Visa', 4111432143214321, TO_DATE('05-2020', 'MM-YYYY'),
                   4005);
insert into rental(6, 6, 'EE123111',
                   TO_DATE('16-NOV-2003', 'DD-MM-YYYY'), TO_DATE('06-JAN-2004', 'DD-MM-YYYY'), TO_DATE('06:06 AM', 'HH:MI AM'), TO_DATE('06:16 PM', 'HH:MI PM')
                   30,
                   'Visa', 4111543254325432, TO_DATE('06-2020', 'MM-YYYY'),
                   4006);
insert into rental(7, 7, 'EE123111',
                   TO_DATE('04-FEB-2004', 'DD-MM-YYYY'), TO_DATE('15-JUN-2003', 'DD-MM-YYYY'), TO_DATE('04:52 AM', 'HH:MI AM'), TO_DATE('00:23 PM', 'HH:MI PM')
                   35,
                   'Visa', 4111654365436543, TO_DATE('07-2020', 'MM-YYYY'),
                   4007);
insert into rental(8, 8, 'EE123111',
                   TO_DATE('08-AUG-2008', 'DD-MM-YYYY'), TO_DATE('09-SEP-2009', 'DD-MM-YYYY'), TO_DATE('08:48 AM', 'HH:MI AM'), TO_DATE('09:39 PM', 'HH:MI PM')
                   40,
                   'Amex' 3222123443211234, TO_DATE('08-2020', 'MM-YYYY'),
                   3008);
insert into rental(9, 9, 'EE123111',
                   TO_DATE('09-SEP-2009', 'DD-MM-YYYY'), TO_DATE('09-SEP-2009', 'DD-MM-YYYY'), TO_DATE('09:40 PM', 'HH:MI PM'), TO_DATE('10:40 PM', 'HH:MI PM')
                   45,
                   'Amex', 3222234554322345, TO_DATE('09-2020', 'MM-YYYY'),
                   3009);

insert into return(1,
                   TO_DATE('01-MAR-2001','DD-MM-YYYY'), TO_DATE('04:40 PM', 'HH:MI PM'),
                   15, 1, 41);
insert into return(2,
                   TO_DATE('02-APR-2001', 'DD-MM-YYY'), TO_DATE('11:40 AM', 'HH:MI AM'),
                   20, 0, 42);
insert into return(3,
                   TO_DATE('31-DEC-2003', 'DD-MM-YYYY'), TO_DATE('11:59 PM', 'HH:MI PM'),
                   25, 0, 43);
insert into return(4,
                   TO_DATE('17-AUG-2014', 'DD-MM-YYYY'), TO_DATE('07:30 PM', 'HH:MI PM'),
                   30, 0, 44);
insert into return(5,
                   TO_DATE('29-JUL-2005', 'DD-MM-YYYY'), TO_DATE('01:00 PM', 'HH:MI PM'),
                   35, 0, 45);
insert into return(6,
                   TO_DATE('06-JAN-2004', 'DD-MM-YYYY'), TO_DATE('06:16 PM', 'HH:MI PM'),
                   40, 0, 46);
insert into return(7,
                   TO_DATE('15-JUN-2003', 'DD-MM-YYYY'), TO_DATE('00:23 PM', 'HH:MI PM'),
                   45, 0, 47);
insert into return(8,
                   TO_DATE('09-SEP-2009', 'DD-MM-YYYY'), TO_DATE('09:39 PM', 'HH:MI PM'),
                   50, 0, 48);
insert into return(9,
                   TO_DATE('09-SEP-2009', 'DD-MM-YYYY'), TO_DATE('10:40 PM', 'HH:MI PM'),
                   55, 0, 49);

