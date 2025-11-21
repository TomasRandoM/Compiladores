start {
  Int a;
  Str str1, str2, str3;
  Bool c;
  a = 0;
  c = true;
  if (!c){
    while (a <= 10){
      a = ++a;
    }
    ret a;
  }
  else {
    str1 = "string1";
    ret str1;
  }
}