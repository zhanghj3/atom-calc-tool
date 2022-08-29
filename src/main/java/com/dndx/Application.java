package com.dndx;

import com.dndx.entity.Atom;
import com.dndx.entity.TimestampAtomVO;
import com.dndx.entity.TimestampResult;
import com.dndx.page.StartPage;
import com.dndx.util.ExcelUtils;
import com.dndx.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dndx.data.Constans.oriData;

public class Application {

    public static int scale = 10;

    public static void main(String[] args) {
        // 启动开始控件
        StartPage.begin();
    }

    /**
     * 开始执行
     */
    public static void exec(){
        // 获取文件数据
        String oriData = FileUtils.readFileByLines("./out.xyz");
        List<TimestampResult> timestampResults = new ArrayList<>();
        List<TimestampAtomVO> timestampAtomVOList = dealOridata(oriData);
        for (TimestampAtomVO timestampAtomVO : timestampAtomVOList) {
            TimestampResult timestampResult = new TimestampResult();
            timestampResult.setTimestamp(timestampAtomVO.getTimestamp());
            List<Atom> atoms = timestampAtomVO.getAtoms();
            if (timestampAtomVO.getCount() != atoms.size()) {
                System.out.println("when timestamp is " + timestampAtomVO.getTimestamp() + ", data error !!!");
                break;
            }
            System.out.println("##### timestamp=" + timestampAtomVO.getTimestamp() + " #####");

            // 每个原子算出来的结果求和
            BigDecimal amountResult = BigDecimal.ZERO;
            for (int i = 0; i < atoms.size(); i++) {
                // 距离平方和
                BigDecimal amountDoubleDistance = BigDecimal.ZERO;
                // 距离和
                BigDecimal amountDistance = BigDecimal.ZERO;
                for (int j = 0; j < atoms.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    BigDecimal distance = calcDistance(atoms.get(i), atoms.get(j));
                    amountDistance = amountDistance.add(distance);
                    amountDoubleDistance = amountDoubleDistance.add(distance.multiply(distance));
                }
                // N-1
                BigDecimal n_1 = BigDecimal.valueOf(timestampAtomVO.getCount() - 1);
                // 分子开方
                BigDecimal fzKai2Fang = kai2Fang(amountDoubleDistance.divide(n_1, 10, RoundingMode.HALF_UP).subtract((amountDistance.divide(n_1, 10, RoundingMode.HALF_UP)).multiply(amountDistance.divide(n_1, 10, RoundingMode.HALF_UP))));
                // 分母
                BigDecimal fm = amountDistance.divide(n_1, 10, RoundingMode.HALF_UP);
                // 每个原子算出来的结果
                BigDecimal result = fzKai2Fang.divide(fm, 10, RoundingMode.HALF_UP);
                amountResult = amountResult.add(result);
            }
            // N个原子算出来的结果求和后 -> 除N -> 四舍五入保留10位小数
            amountResult = amountResult.divide(BigDecimal.valueOf(atoms.size()), scale, RoundingMode.HALF_UP);
            timestampResult.setResult(amountResult.toString());
            timestampResults.add(timestampResult);
        }
        // 导出excel
        ExcelUtils.writeExcel(timestampResults, "ZWJ");
        // 打印结果
        for (TimestampResult timestampResult : timestampResults) {
            System.out.println(timestampResult);
        }
    }

    /**
     * 处理数据
     *
     * @param oriData
     * @return
     */
    public static List<TimestampAtomVO> dealOridata(String oriData) {
        List<String> lines = Arrays.asList(oriData.split("\n")).stream().filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList());
        List<TimestampAtomVO> timestampAtomVOList = new ArrayList<>();
        TimestampAtomVO timestampAtomVO = new TimestampAtomVO();
        List<Atom> atoms = new ArrayList<>();
        // timestamp的行
        int tsIndex = 0;
        int count = 0;
        for (int i = 0; i < lines.size(); i++) {
            String[] s = lines.get(i).split(" ");
            if (s.length == 1) {
                count = Integer.parseInt(s[s.length - 1]);
                if (i != 0) {
                    timestampAtomVO.setCount(count);
                    timestampAtomVO.setAtoms(atoms);
                    timestampAtomVOList.add(timestampAtomVO);
                }
                timestampAtomVO = new TimestampAtomVO();
                atoms = new ArrayList<>();
                tsIndex = i + 1;
                continue;
            }
            if (i == tsIndex) {
                timestampAtomVO.setTimestamp(Integer.parseInt(s[s.length - 1]));
                continue;
            }
            BigDecimal x = s[s.length - 3].contains("e-") ? BigDecimal.ZERO : new BigDecimal(s[s.length - 3]);
            BigDecimal y = s[s.length - 2].contains("e-") ? BigDecimal.ZERO : new BigDecimal(s[s.length - 2]);
            BigDecimal z = s[s.length - 1].contains("e-") ? BigDecimal.ZERO : new BigDecimal(s[s.length - 1]);
            atoms.add(new Atom(x, y, z));
            if (i == lines.size() - 1) {
                timestampAtomVO.setCount(count);
                timestampAtomVO.setAtoms(atoms);
                timestampAtomVOList.add(timestampAtomVO);
            }
        }
        return timestampAtomVOList;
    }

    /**
     * 计算两个坐标的距离
     *
     * @param atom1
     * @param atom2
     */
    public static BigDecimal calcDistance(Atom atom1, Atom atom2) {
        BigDecimal xx = atom1.getX().subtract(atom2.getX());
        BigDecimal yy = atom1.getY().subtract(atom2.getY());
        BigDecimal zz = atom1.getZ().subtract(atom2.getZ());
        return BigDecimal.valueOf(Math.sqrt(xx.multiply(xx).add(yy.multiply(yy)).add(zz.multiply(zz)).doubleValue()));
    }

    public static BigDecimal kai2Fang(BigDecimal x) {
        return BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
    }


}
