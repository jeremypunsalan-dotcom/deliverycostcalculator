DROP TABLE IF EXISTS rules;
 
CREATE TABLE rules (
  ruleid INT AUTO_INCREMENT  PRIMARY KEY,
  rulename VARCHAR(255) NOT NULL,
  ruledescription VARCHAR(255) DEFAULT NULL,
  rulepriority INT NOT NULL,
  rulecondition VARCHAR(255) NOT NULL,
  ruleaction VARCHAR(255) NOT NULL,
  createdate TIMESTAMP,
  updatedate TIMESTAMP
);

INSERT INTO rules (rulename, ruledescription, rulepriority, rulecondition, ruleaction, createdate) VALUES
  ('Reject', 'Weight exceeds 50kg', 1, 'delivery.weight > 50', 'delivery.cost = null;', CURRENT_TIMESTAMP()),
  ('Heavy Parcel', 'Weight exceeds 10kg', 2, 'delivery.weight > 10', 'delivery.cost = delivery.weight * 20;', CURRENT_TIMESTAMP()),
  ('Small Parcel', 'Volume is less than 1500 cm3', 3, 'delivery.volume < 1500', 'delivery.cost = delivery.volume * 0.03;', CURRENT_TIMESTAMP()),
  ('Medium Parcel', 'Volume is less than 2500 cm3', 4, 'delivery.volume < 2500', 'delivery.cost = delivery.volume * 0.04;', CURRENT_TIMESTAMP()),
  ('Large Parcel', null, 5, 'true', 'delivery.cost = delivery.volume * 0.05;', CURRENT_TIMESTAMP());
  
