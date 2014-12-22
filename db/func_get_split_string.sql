DROP FUNCTION IF EXISTS MovieSearch.func_get_split_string;
CREATE DEFINER=`sa`@`%` FUNCTION `func_get_split_string`(
  f_string varchar(1000) CHARACTER SET UTF8,f_delimiter varchar(5) CHARACTER SET UTF8,f_order int) RETURNS varchar(255) CHARSET utf8
BEGIN
-- Get the separated number of given string.
    declare result varchar(255) CHARSET UTF8 default '' ;
    set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1));
    return result;
  END;