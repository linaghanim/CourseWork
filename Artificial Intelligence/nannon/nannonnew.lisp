;Lina Ghanim
;HW 2

(Defparameter *DIE* 6);3
(Defparameter *pos* 6);3
(Defparameter *ch* 6);2
(Defparameter *start* '(0 1 2 5 6 7))
					;0--> beg
					;7--> won

(defun spaces (n) (loop for i from 1 to n do (princ " ")))

(Defun print_board (board &optional (position 0)) 
  (cond ((<= position (* 2 *pos*))
	 (if (= position 1) (princ "<")
           (if (= position (+ 1 *pos*)) (princ ">")))
	   (if (> (count position board) 0)
               (loop for piece from 0 to (- *ch* 1) do
		     (if (=  (nth piece board) position)
			 (if (> piece (- (/ *ch* 2) 1)) (princ "B")
			   (princ "A"))))
             (princ "-"))
	   (print_board board (1+ position)))
	 (T (terpri)
            (spaces (max 2 (+ 1 (count 0 board))))
          (terpri))))

(defun print_board-old (board &optional (position 0))
  (cond ((<= position 7)
	 (if (= position 1) (princ "<")
           (if (= position 7)(princ ">")))
	 (if (> (count position board) 0)
             (loop for piece from 0 to 5 do
		   (if (=  (nth piece board) position)
                       (if (> piece 2) (princ  (nth piece board))
			 (princ (nth piece board)))))
           (princ "-"))
	 (print_board board (1+ position)))
	(T (terpri)
           (spaces (max 2 (+ 1 (count 0 board))))
           (princ "123456")(terpri))))


(defun roll () 
      (+ (random *DIE*) 1))

(defun firstroll () 
(let ((p1 (roll)) (p2 (roll)))
(if (= p1 p2) (firstroll) (abs (- p1 p2)))))

(defun dicestream (n) ;
(if (= n 0) nil
(cons (roll) (dicestream (- n 1)))))


(defun swap (pos);swap spots 1->7 / 2->6 ...
  (if (null pos) '()
    (cons (- (+ 1 *pos*) (car pos)) (swap (cdr pos)))))

(defun swapplayer (pos) 
  (append (subset1 (- (/ (length pos) 2) 1) (reverse (swap pos)))  (subset1 (- (/ (length pos) 2) 1) (swap pos))))


(defun allsame (lst);a person wins if all his positions are > *ch* +1
  (cond ((null lst) t)
	((not (> (car lst) *ch*)) nil)
	(t (allsame (cdr lst)))))



(defun subset1 (to pos);create list of player 1 current positions 
  (if (= to 0) (list (car pos))
	(cons (car pos) (subset1 (- to 1) (cdr pos)))))
;(subset 2 '(0 1 2 5 6 7))

(defun whowon (pos) 
  (let ((p1 (subset1 (- (/ (length pos) 2) 1) pos)) (p2 (subset1 (- (/ (length (swapplayer pos)) 2) 1)  (swapplayer pos))))
    (cond ((allsame p1) 1)
	  ((allsame p2) 2)
	  (t 0))))

(defun legalmoves (pos roll) ;list of what positions it can move from 
  (let ((from (subset1 (- (/ (length pos) 2) 1) pos))) ;current positions
  (remove nil (legalfrom (deletewinnermove from) pos roll))))
; (legalmoves '(0 1 2 5 6 7) 1)

(defun deletewinnermove (lst)
  (cond ((null lst) nil)
	((> (car lst) *pos*) (deletewinnermove (cdr lst)))
	(t (cons (car lst) (deletewinnermove (cdr lst))))))
    

(defun notexist? (lst a) ;return false if exists
  (cond ((null lst) t)
	((= (car lst) a) nil)
	(t (notexist? (cdr lst) a))))

(defun condition1 (from to pos) ; to: 1 number / from: positions of player 1
  (cond ((> to *pos*) t) ;got to other side
      ((notexist? pos to) t) ;empty spot
      ((not (notexist? from to)) nil) ;my checker is in that position
      ((or (not (notexist? (subset1  (- (/ (length pos) 2) 1) (reverse pos)) (+ 1 to)))
	   (not (notexist? (subset1  (- (/ (length pos) 2) 1) (reverse pos)) (- to 1)))) nil);partner is in position and has 2 next each other
      (t t))) ;they will kick  someone


(defun legalfrom (from pos roll) ;return list of legal from
  (let ((to (mapcar (lambda (x) (+ x roll)) from)))
    (loop for i in to collect (when (condition1 from i pos) (- i roll)))
    ))
;(legalfrom '(0 1 2) '(0 1 2 5 6 7) 1)



(defun makemoveother (pos checker roll)
(cond ((null pos) nil)
      ((= (car pos) (+ checker roll)) (cons (+ 1 *pos*) (makemoveother (cdr pos) checker roll))) ; move opponent off
      (t (cons (car pos) (makemoveother (cdr pos) checker roll)))))

(defun makemove (pos checker roll) ;checker position: 0 off board / 1 to *pos* on board -works
  ;(print checker)
    (cond ((null pos) nil)
	  ((= (car pos) checker) (if (> (+ checker roll) *pos*)
				     (cons (+ 1 *pos*) (makemoveother (cdr pos) checker roll))
				     (cons (+ checker roll) (makemoveother (cdr pos) checker roll)))) ;reset our position
	  (t (cons (car pos) (makemove (cdr pos) checker roll)))))
					;(makemove '(0 1 2 5 6 7) 1 3)


;----------------------
;part 2

(defparameter *hasht* (make-hash-table :test 'equal))


(Defun children (pos)
  ;(print pos)
  (loop for roll from 1 to *DIE* append ;*DIE*
					;(print roll)
	(let ((moves (legalmoves pos roll)))
	  ;(print moves)
	(loop for move in moves collect
	    
	      (makemove pos move roll)))))

; (explore '(0 1 2 5 6 7))
(defun explore (pos);start with starting position
					;(print que)
  (let ((sortpos (sortposition pos)))
  (unless (gethash sortpos *hasht*)
   ;(print pos)
       (setf (gethash sortpos *hasht*) (whowon pos))
       (loop for position in (children sortpos) do (explore position))
       (explore (swapplayer sortpos))
       )))

(defun sortposition (pos) ;sort checker positions because 1,2,3=3,2,1=any other combination of positions
  (append (sort (subset1 (- (/ (length pos) 2) 1) (reverse pos))  #'<) (sort (subset1 (- (/ (length pos) 2) 1) pos)  #'<)))

  ;(maphash #'print-hash-entry *hasht*)
(defun print-hash-entry (key value)
  (format t "The value associated with the key ~S is ~S~%" key value))


;----------------------
;part 3

(defun player1play (pos ds p1 p2)
  (let ((winner (whowon pos)))
    (print winner)
    (if (not (= 0 winner)) (progn (print pos) winner)
      (let ((newpos (funcall p1 pos (car ds))))
	(if (null newpos) (player1play (swapplayer pos) (cdr ds) p2 p1)
	    (player1play (swapplayer newpos) (cdr ds) p2 p1))))))


(defun playgame (p1 p2 &optional dicestream) 
 (let ((w (player1play *start* dicestream p1 p2))) (progn (princ "The winner is: ") (print w))))

;  (Playgame 'human 'randplay (dicestream 100))
;  (Playgame 'human 'human (dicestream 100))


(defun pick (lst)
  (print lst)
  (if (null lst) lst
    (nth (random (length lst)) lst)))
  

  (defun human (pos roll &aux move)
  ;(print pos)
  ;(print_board pos)
  (princ "you rolled: ")(princ roll)(princ " move? ")(terpri)
  (setf move (read))
  (if (member move (legalmoves pos roll))
      (makemove pos move roll)
    (progn (princ "try again")(terpri)(human pos roll))))

(defun randplay (board roll)
  (let ((move (pick (legalmoves board roll))))
    (print move)
    (if (null move) board
      (makemove board move roll))))

;----------------------------------

 (defun playturn2 (p1 p2 s1 s2 n)
    (if (= n 0) (list (/ (- s1 s2)(+ s1 s2)))
      (let ((ds (dicestream 100)))
	(let ((winner1 (playgame p1 p2 ds)) (winner2 (playgame p2 p1 ds))) ;create ds to share for both games 
	      (cond ((and (= winner1 1) (= winner2 1)) (playturn2 p1 p2 (+ s1 2) s2 (- n 1)))
		    ((and (= winner1 2) (= winner2 2)) (playturn2 p1 p2 s1 (+ s2 2) (- n 1)))
		    (t (playturn2 p1 p2 (+ 1 s1) (+ 1 s2) (- n 1)))))))) ;tie

(defun playtourn (p1 p2 &optional n)
  (playturn2 p1 p2 0 0 n))
 ;(Playtourn 'human 'human 10)


(defparameter *scores* (make-hash-table))
  
(defun roundrobin (lst &optional n)
    (if (< n 0) (maphash #'print-hash-entry *scores*)
      (progn
	(loop for i from 0 to (- (/ (length lst) 2) 1) do
	    (let ((p1 (nth i lst)) (p2 (nth (- (length lst) 1) lst)))
	    (let ((winner (playgame p1 p2 (dicestream 100))))
	     ; (print winner)
	      (cond ((= winner 1) (setf (gethash p1  *scores*) (Add1 (gethash p1  *scores*))))
		    ((= winner 2) (setf (gethash p2  *scores*) (Add1 (gethash p2 *scores*))))))))
       ;(princ "cc")
       (roundrobin (append (list (car lst)) (rrotate (cdr lst))) (- n 1)))))
;(roundrobin (list 'randplay 'firstplay) 10)


(defun Add1 (val)
  (if (null val) 1 (+ 1 val)))

(defun rrotate (l) (cons (car (last l))(butlast l)))
  


;-------------------------

 
 (defun highestlegalmove (sub pos roll)
   (makemove pos (car sub) roll))
   

(defun firstplay (pos roll);furthest forward 
  (print_board pos)
  (princ "you rolled: ")(princ roll)
  (let ((moves (legalmoves pos roll)))
    (if moves (highestlegalmove (sort moves  #'<) pos roll) nil)))
 
  ; (firstplay '(0 1 2 5 6 7) 1)
 
  (defun lastplay (pos roll)
  (print_board pos)
  (princ "you rolled: ")(princ roll)
   (let ((moves (legalmoves pos roll)))
    (if moves (highestlegalmove (sort moves  #'>) pos roll) nil))) ;reverse

  
  (defun scoreplay (pos roll)
  (print_board pos)
  (princ "you rolled: ")(princ roll)
  
  (makemove pos (maximum (legalmoves pos roll) pos 0 0 roll) roll))

  (defun maximum (me pos max m roll)
    (if (null me) m
      (let ((move (car me)))
	(let ((newpos (makemove pos move roll)))
	  (let ((meyou (me_you newpos)))
	    (if (> meyou max) (maximum (cdr me) pos meyou (car me) roll)
	      (maximum (cdr me) pos max m roll)))
	))))
  
 (defun me_you (pos) ;me - you
    (sum 0 (/ (length pos) 2)  pos))

(defun sum (i mid pos)
(if (null pos) 0
  (if (< i mid) (+ (car pos) (sum (+ 1 i) mid (cdr pos)))
    (- (car pos) (sum (+ 1 i) mid (cdr pos))))))

;-----------------------------
;Part 4

(defun mystrategy (me pos roll) ;take the move that would get you off the board, if none then take one that is lastplay
    (if (null me) (lastplay pos roll)
	  (if (< *pos* (+ (car me) roll)) (makemove pos (car me) roll) (mystrategy (cdr me) pos roll))))

  
(defun myplay (pos roll)
  (print_board pos)
  (princ "you rolled: ")(princ roll)
  (mystrategy (legalmoves pos roll) pos roll))
