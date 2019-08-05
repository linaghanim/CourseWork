;2.27

(define (deep-reverse x)
  (cond ((null? x) x)
        ((pair? (car x)) (append (deep-reverse (cdr x)) (list (deep-reverse (car x)))))
        (else (append (deep-reverse (cdr x)) (list (car x))))))


;2.28

(define (fringe x)
  (cond ((null? x) x)
        ((pair? (car x)) (append (fringe (car x)) (fringe (cdr x))))
        (else x)))