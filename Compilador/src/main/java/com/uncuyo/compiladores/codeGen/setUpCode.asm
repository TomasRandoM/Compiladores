.data
    addOne: .double 1.0
    zeroDouble: .double 0.0
.text
    lessDouble:
        c.lt.d $f2, $f0
        li $a0, 1
        bc1t endDouble
        li $a0, 0
        j endDouble

    greaterDouble:
        c.lt.d $f0, $f2
        li $a0, 1
        bc1t endDouble
        li $a0, 0
        j endDouble

    equalDouble:
        c.eq.d $f2, $f0
        li $a0, 1
        bc1t endDouble
        li $a0, 0
        j endDouble

    notEqualDouble:
        c.eq.d $f2, $f0
        li $a0, 0
        bc1t enddouble
        li $a0, 1
        j enddouble

    greaterEqualDouble:  #!(f2<f0) f2>=f0
        c.lt.d $f2, $f0
        li $a0, 0
        bc1t endDouble
        li $a0, 1
        j endDouble


    lessEqualDouble: #f2 <= f0
        c.lt.d $f0, $f2
        li $a0, 0
        bc1t endDouble
        li $a0, 1
        j endDouble

    endDouble:
        jr $ra
