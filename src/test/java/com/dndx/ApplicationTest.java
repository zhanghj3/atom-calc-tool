package com.dndx;

import com.dndx.entity.Atom;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ApplicationTest {

    @Test
    public void test_calcDistance() {
        Atom atom1 = new Atom(new BigDecimal("1.43249"), new BigDecimal("0.24324"), new BigDecimal("-1.23452"));
        Atom atom2 = new Atom(new BigDecimal("3.08242"), new BigDecimal("2.23532"), new BigDecimal("-0.08920"));
        //平方和：2.7222690049+3.9683827264+1.3117579024=8.0024096337
        //开根号（保留10位小数）：8.0024096337->2.8288530598
        BigDecimal result = Application.calcDistance(atom1, atom2);
        System.out.println(result);
        Assert.assertEquals(new BigDecimal("2.8288530598"), result.divide(new BigDecimal(1), 10, RoundingMode.HALF_UP));
    }
}
