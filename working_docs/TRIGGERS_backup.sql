-- CUSTOMER ORDER TABLE - AFTER INSERT

CREATE DEFINER=`root`@`%` TRIGGER `setInsolvency` AFTER INSERT ON `customer_order` FOR EACH ROW BEGIN   
/* Set insolvency status if the new order that was inserted was rejected */
IF new.status = "REJECTED" THEN  
UPDATE user u  SET u.insolvent = 1 
WHERE new.user_id = u.user_id; 
END IF;   
END

------------------------------------------------------------------------------------------------------
-- CUSTOMER ORDER TABLE - AFTER UPDATE

CREATE DEFINER=`root`@`%` TRIGGER `setAndCheckInsolvency` AFTER UPDATE ON `customer_order` FOR EACH ROW BEGIN
/* PREREQ: Upon INSERT into USER table, default for the insolvent column is 0.
Then after every update in the ORDER table, we check all the customer_orders and make
sure that the insolvent flag remains 1 so long as there exists a rejected order
associated with the user whereas the flag turns to 0 ONLY IF none of the orders associated
with the user are rejected (expressed by the NOT IN expression) */

UPDATE user u
LEFT JOIN customer_order co
ON co.user_id = u.user_id
SET u.insolvent = 
CASE 
	WHEN co.status = 'REJECTED' THEN 1
	WHEN co.status NOT IN ('REJECTED') THEN 0
	ELSE 1
END;
END


------------------------------------------------------------------------------------------------------
-- OPTIONAL PRODUCT ORDERDER TABLE - AFTER INSERT

CREATE DEFINER=`root`@`%` TRIGGER `optional_product_ordered_AFTER_INSERT` AFTER INSERT ON `optional_product_ordered` FOR EACH ROW BEGIN

DECLARE quantity integer;
UPDATE optional_product AS p
SET p.quantity_sold = 1 + (SELECT quantity_sold 
					FROM optional_product AS p 
					WHERE p.opt_id = new.opt_id);


END


------------------------------------------------------------------------------------------------------
-- USER TABLE - AFTER UPDATE

CREATE DEFINER=`root`@`%` TRIGGER `checkForAlert` AFTER UPDATE ON `user` FOR EACH ROW BEGIN
/*TODO get the timestamp? */

IF(
(SELECT COUNT(co.status) 
FROM customer_order co 
WHERE co.user_id = new.user_id AND co.status = 'REJECTED') >= 3) 
THEN SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'ALERT: 3 or more rejected orders for this user';
END IF;

END