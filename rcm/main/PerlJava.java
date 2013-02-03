
package rcm.main;
//
//import perl5. ;
//import perl5.PLJava;
//import perl5.SV ;
//
//
//
///**
// *
// * @author Krishna Sapkota, 31-Dec-2011,   23:45:50
// * A PhD project at Oxford Brookes University
// */
//public class PerlJava {
//
//    public static void main(String argv[]) {
//
//       Perl.eval("print `Hello World`;") ;
//       PLJava.PLJava_eval("Krishna");

//       ///////////////////
//
//       SV foo = Perl.NEW("foo") ; // $foo = new foo() ;
//
//       foo.call("subtest") ;  // $foo->subtest() ;
//
//       ///////////////////
//
//       String s = Perl.eval(" 'time: ' + time() ") ;
//
//       int i = Perl.eval_int(" 2**10 ") ; // 1024
//
//       int n = Perl.eval_int(" 10/3 ") ; // 3
//       int d = (int) Perl.eval_double(" 10/3 ") ; // 3.33333333333333
//
//       ///////////////////
//
//       SV array = Perl.eval_sv("  [ 'a' , 'b' , 'c' ]  ") ;
//
//       String e0 = array.elem(0) ; // a
//       String e1 = array.elem(1) ; // b
//       String e2 = array.elem(2) ; // c
//
//       ///////////////////
//
//       SV hash = Perl.eval_sv("  { a => 11 , b => 22 , c => 33 }  ") ;
//
//       String k_a = hash.key("a") ; // 11
//       String k_b = hash.key("b") ; // 22
//       String k_c = hash.key("c") ; // 33

//     }
//
//
//}
