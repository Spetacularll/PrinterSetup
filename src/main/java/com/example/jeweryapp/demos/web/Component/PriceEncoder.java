package com.example.jeweryapp.demos.web.Component;

import java.util.Scanner;

public class PriceEncoder {
    // 数字到字符的映射表
    private static final char[] ENCODING = {'0','A', 'R', 'E', 'S', 'W', 'L', 'T', 'B', 'J'};

    /**
     * 将价格编码为字符串格式
     *
     * @param price 输入价格（1000 - 500000）
     * @return 编码后的字符串
     */
    public static String encodePrice(int price) {
        if (price < 100 || price > 700000 || price % 100 != 0) {
            throw new IllegalArgumentException("价格必须是100到700000之间，并且是100的倍数");
        }

        StringBuilder encoded = new StringBuilder();

        // 去掉千位后的部分，进行编码
        int remaining = price / 1000;
        int flag = remaining ;
        boolean trailingZero = true; // 是否在处理尾随零

        while (remaining > 0) {
            int digit = remaining % 10; // 取当前千位
            remaining /= 10; // 减少一个千位
                char encodedChar = ENCODING[digit]; // 映射字符 (1对应A)
                encoded.insert(0, encodedChar); // 添加到前面
                trailingZero = false; // 非零位后，尾随零标志无效
        }

        // 如果编码长度为1，则补充前缀0
        if (flag<10) {
            encoded.insert(0, '0');
        }
        // 如果编码长度为2，则在中间插入·
        if (encoded.length() == 2) {
            encoded.insert(1, '·');
        }

        return encoded.toString();
    }

    public static void main(String[] args) {
        // 示例测试
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String output = encodePrice(Integer.parseInt(input));
            System.out.println(output);
        }


    }
}
