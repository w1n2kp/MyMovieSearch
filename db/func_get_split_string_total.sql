CREATE FUNCTION func_get_split_string_total (
f_string varchar(1000),f_delimiter varchar(5)
) RETURNS int(11)
BEGIN
  -- Get the total number of given string.
  return 1+(length(f_string) - length(replace(f_string,f_delimiter,'')));
END