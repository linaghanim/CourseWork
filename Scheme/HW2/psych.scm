;; This is the code for ``Computer Psychiatrist'' (Doctor)

; for Racket users...
(#%require (only racket/base random))
; *******************

(define (visit-doctor1 name)
  (write-line (list 'hello name))
  (write-line '(what seems to be the trouble?))
  (doctor-driver-loop name '()))

;#4
(define (visit-doctor)
  (let ((user-response (ask-patient-name)))
               (if (not (equal? user-response '(stop))) ;type (stop) to stop the loop
                   (visit-doctor1 user-response) (write-line '(we are closed)) )
                   ))

#| old version

(define (doctor-driver-loop name)
  (newline)
  (write '**)
  (let ((user-response (read)))
    (cond ((equal? user-response '(goodbye))
             (write-line (list 'goodbye name))
             (write-line '(see you next week)))
          (else (write-line (reply user-response))
                (doctor-driver-loop name)))))

(define (reply user-response)
  (cond ((fifty-fifty)
           (append (qualifier)
                   (change-person user-response)))
        (else (hedge))))
|#

;#3 + #4 update
(define (doctor-driver-loop name responses)
  (newline)
  (write '**)
  (let ((user-response (read)))
    (cond ((equal? user-response '(goodbye))
             (write-line (list 'goodbye name))
             (write-line '(see you next week))
             (visit-doctor))
          ((equal? user-response '(stop))
             (write-line '(we are closed)))
          (else (write-line (reply user-response responses))
                (doctor-driver-loop name (cons user-response responses))))))

;#3 updated
(define (reply user-response responses)
  (if (or (equal? responses '()) (fifty-fifty))
         (cond ((fifty-fifty)
                (append (qualifier)
                   (change-person user-response)))
               (else (hedge)))
        (past responses)))

  (define (past responses)
    (append '(earlier you said that) (nth (random (length responses)) responses)) 
     )
  
(define (fifty-fifty)
  (= (random 2) 0))

(define (insert-last e lst)
  (let helper ((lst lst))
    (if (pair? lst)
        (cons (car lst)
              (helper (cdr lst)))
        (cons e '()))))

(define (qualifier)
  (pick-random '((you seem to think)
                 (you feel that)
                 (why do you believe)
                 (why do you say)
                 (why do you think)
                 (do you really think that))))

;#1 updated
(define (hedge)
  (pick-random '((please go on)
                 (many people have the same sorts of feelings)
                 (many of my patients have told me the same thing)
                 (please continue)
                 (tell me more...)
                 (what else?))))

(define (replace pattern replacement lst)
  (cond ((null? lst) '())
        ((equal? (car lst) pattern)
           (cons replacement
                 (replace pattern replacement (cdr lst))))
        (else (cons (car lst)
              (replace pattern replacement (cdr lst))))))

(define (many-replace replacement-pairs lst)
  (cond ((null? replacement-pairs) lst)
         (else (let ((pat-rep (car replacement-pairs)))
            (replace (car pat-rep)
                     (cadr pat-rep)
                     (many-replace (cdr replacement-pairs)
                     lst))))))

;#2 updated
(define (change-person phrase)
  (many-replace2 '((are am) (you i) (your my) (i you) (me you) (am are) (my your))
                phrase))


(define (pick-random lst)
  (nth (+ 1 (random (length lst))) lst))

;#2 my method
(define (many-replace2 replacement-pairs lst)
  (cond ((null? lst) lst)
         (else
           (cons (replace2 (car lst) replacement-pairs)
                      (many-replace2 replacement-pairs (cdr lst))))))


(define (replace2 word2 replacement-pairs)
  (if (null? replacement-pairs)
         ((lambda (x) x) word2)
        (let ((pat-rep (car replacement-pairs)))
            (if (equal? word2 (car pat-rep)) (cadr pat-rep)
                        (replace2 word2 (cdr replacement-pairs))))))

;(change-person '(you are not being very helpful to me))
;;******

(define (prob n1 n2)
  (< (random n2) n1))

(define (ask-patient-name)
  (write-line '(next!))
  (write-line '(who are you?))
  (car (read)))

(define (nth n lst)
  (if (= n 1) 
      (car lst)
      (nth (- n 1) (cdr lst))))
;;******

(define (atom? a) (not (pair? a)))

(define nil '())

(define (write-line x) (begin (write x) (newline)))

;;******

#|

#2
(change-person ’(you are not being very helpful to me))
result1 : (you are not being very helpful to you)
result adding at end : (you are not being very helpful to you)
result adding to beginning : (i am not being very helpful to i)

yes it matters. since the replace-all method calls recursively on the rest of the change-person ways, it will first change the last pairs and end with the first pair. So the first pair in the list is the last updated for all words in the sentence. 
|#