start
{
  Bool a;
  Bool b;
  Bool c;

  a = true;
  b = false;
  a = b && a;
  b = a || b;
  a = (a && b) || (b && (a || b));
}