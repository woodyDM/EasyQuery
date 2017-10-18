
CREATE TABLE superuser (
  id int(10) NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL ,
  createTime datetime DEFAULT NULL,
  bigDecimal decimal(10,3) DEFAULT NULL,
  point1 double(10,3) DEFAULT NULL,
  point2 double(10,3) DEFAULT NULL,
  point3 float(10,3) DEFAULT NULL,
  point4 float(10,3) DEFAULT NULL,
  ok1 int(2) DEFAULT NULL,
  ok2 bit(1) DEFAULT NULL,
  updateDate date DEFAULT NULL,
  PRIMARY KEY (id)
) ;

INSERT INTO superuser VALUES ('1', 'name1', '2017-10-18 13:57:05', '1.300', '1.500', '1.500', '1.400', '1.200', '1', '\0', '2017-10-18');
INSERT INTO superuser VALUES ('2', 'name2', '2017-10-18 13:57:05', '1.400', '1.500', '1.500', '1.400', '1.200', '1', '\0', '2017-10-18');
