package pst.arm.client.common.ui.form;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public enum VType {
  ALPHABET("^[a-zA-Z_]+$", "Alphabet "), 
  ALPHABETNOTEMPTY("^[a-zA-Z][\\S]+$", "Должны быть только буквы латиницы"), 
  ALPHANUMERIC("^[a-zA-Z0-9_]+$", "Alphanumeric "), //Alphanumeric
  NUMERIC("^[+0-9]+$", "Число задано не верно"), //Numeric
  EMAIL("^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,4}$", "E-mail задан не верно"),
  EMAIL2("^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:[.][-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?[.])*(?:aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$", "E-mail задан не верно"),
  NOTFIRSTEMPTY("^[\\S]+[\\s\\S]*$", "Не должно начинаться с пробела"),
  DIGIT("^[0-9]+$", "Должны быть только цифры"), //Digit
  DIGITINN("(^[0-9]{10}|^[0-9]{12})+$", "Должнo быть 10 или 12 цифр"), //Digit 10 or 12 for INN
  DIGITINN12("(^[0-9]{12})+$", "Должнo быть ровно 12 цифр"),
  ONEDIGIT("(^[1-9]+[0-9]*|^[0]{1})([,.]{1}[0-9]{1})?$", "Должно быть число с точностью до десятых"),
  TWODIGITS("(^[1-9]+[0-9]*|^[0]{1})([,.]{1}[0-9]{1,2})?$", "Должно быть число с точностью до сотых");
             
  String regex;
  String name;

  VType(String regex, String name) {
    this.regex = regex;
    this.name = name;
  }
}
