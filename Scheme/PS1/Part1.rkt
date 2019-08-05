;Lina Ghanim
;PS1

;1.17
(define (twice x) (+ x x))
(define (half x) (/ x 2))
(define (isEven x) (= (remainder x 2) 0))

(define (mul a b)
  (write (list a b))(newline)
  (if (= 0 a)
      0
      (if (isEven a)
          (mul (half 2) (twice b))
          (+ b (mul (- a 1) b)))))


;1.34
(define (square x) (* x x))

(define (f g)
  (g 2))

#|
application: not a procedure, it expected a procedure that can be applied to arguments. 
|#

;1.43
(define (compose f g)
  (lambda (x) (f (g x))))

(define (repeated f n)
  (cond ((zero? n) (lambda (x) x))
        (else (compose f (repeated f (- n 1))))))

;1.44

(define dx 0.001)
(define (smooth f)
  (lambda (x)
    (/ (+ (f (- x dx))
	  (f x)
	  (f (+ x dx)))
       3)))

(define (nfold-smoothed n)
  (lambda (f x)
   ((repeated (smooth f) n) x)))


;doubling 
(define (double fn) (lambda (x) (fn (fn x))))
(define (1+ x) (+ 1 x))

#|

((double 1+)0)
((lambda (x) (1+ (1+ x))) 0)
(1+ (1+ 0))
2


(((double double) 1+)0)
((((lambda (x) (double (double x))) 1+)0))
((double (double 1+))0)
((double ((lambda (x) (1+ (1+ x)))))0)
((lambda (y) ((lambda (x) (1+ (1+ x))) ((lambda (x) (1+ (1+ x))) y)))0)
((lambda (x) (1+ (1+ x))) ((lambda (x) (1+ (1+ x))) 0))
((lambda (x) (1+ (1+ x))) ((1+ (1+ 0))))
((lambda (x) (1+ (1+ x)))  2)
(1+ (1+ 2))
4

                        
((((double double) double) 1+)0)                                                             
(((double (lambda (x) (double (double x)))) 1+) 0)                                               
(((lambda (x) ((lambda (x) (double (double x))) ((lambda (x) (double (double x))) x))) 1+) 0)   
(((lambda (x) (double (double (double (double x))))) 1+) 0)                                      
((double (double (double (lambda (x) (1+ (1+ x)))))) 0)                                        
((double (double (lambda (x) (1+ (1+ (1+ (1+ x))))))) 0)                                      
(1+ (1+ (1+ (1+ (1+ (1+ (1+ (1+ (1+ (1+ (1+ (1+ (1+ (1+ (1+ (1+ 0))))))))))))))))
16


(((((double double) double) double) 1+)0)
(((((lambda(x) (double(double(x))))double)double)1+)0)
((((double(double double))double)1+)0)
((((lambda(x)(double double (double double (x))))double)1+)0)
((((double double)((double double) double))1+)0)
((((lambda(x) double(double x))(((double double) double)))1+)0)
((((double(double (((double double) double))))1+)0)
(((lambda(x)((double(double (((double double) double))))((double(double (((double double) double)))))(x)))1+)0)
((((double(double ((double double) double)))((double(double ((double double) double))))(1+)))0)
((((double(double ((double double double)))((double(double ((double double double))))))(1+)))0)
((((lambda (x)((double(double ((double double double)))((double(double ((double double double))))))((double(double ((double double double)))((double(double ((double double double)))))) (x))))1+)0)
(((((double(double (double (double (double (double(double (double (double double)))))))))((double(double ((double double double)))((double(double ((double double double)))))) (1+))))0)
((((double(double (double (double (double (double(double (double (double double)))))))))(double(double (double (double (double (double(double (double (double double(double (double (double double))))))))))))(double(double (double (double double))))(double(double (double (double double)))))1+)0)
((((lambda(x) (((double(double (double (double (double (double(double (double (double double)))))))))(double(double (double (double (double (double(double (double (double double(double (double (double double))))))))))))(double(double (double (double double))))(double(double (double (double double))))) (((double(double (double (double (double (double(double (double (double double)))))))))(double(double (double (double (double (double(double (double (double double(double (double (double double))))))))))))(double(double (double (double double))))(double(double (double (double double)))))(x)))1+)0)
(((((((double(double (double (double (double (double(double (double (double (double (double(double (double (double (double (double(double (double (double double(double (double (double( double (double(double (double (double (double (double(double (double (double( double(double(double (double (double (double (double(double (double (double (double(double(double (double (double (double (double(double (double (double double(double (double (double (double(double(double (double (double (double(double(double (double (double( double(............)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))+1)0)
.
.
.
(( 65536 double's ....)+1)0)
2^16 
65536


((((((double double) double) double) double) 1+) 0)
2^65536

this value cannot be computed using the Scheme interpreter because it takes very long time and huge memory- overflow.
 
|#