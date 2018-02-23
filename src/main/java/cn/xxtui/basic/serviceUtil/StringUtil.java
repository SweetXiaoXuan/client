package cn.xxtui.basic.serviceUtil;

import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ValidateMode;
import cn.xxtui.user.utils.ResultMsgConstant;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String工具类
 *
 */
public class StringUtil {
    private static Random randGen = null;
    private static char[] numbersAndLetters = null;
    private static MeaasgeUtil me = new MeaasgeUtil();

    /**
     * 判断参数是否为空
     * @param mapUser 参数集合
     * @return java.lang.String
     */
    public static String isEmpty(Map<String, Object> mapUser) {
        String content = "";
        for (Map.Entry<String, Object> param : mapUser.entrySet()) {
            if (isEmpty("".equals(param.getValue()) || param.getValue() == null ? "" : param.getValue().toString())
                    || isEmpty("null".equals(param.getValue()) ||
                    param.getValue() == "null" ? "" : param.getValue().toString())) {
                content = param.getKey() + me.getValue(ResultMsgConstant.canNotEmpty);
                return content;
            }
        }
        return content;
    }

    /**
     * 查看手机号格式
     * @param phone
     * @param result
     * @return
     */
    public static ResultStruct phoneFormat(String phone, ResultStruct result) {
        // 判断手机号格式是否正确
        if (!ValidateMode.digital(phone)
                || !ValidateMode.length(phone, 11, 11)) {
            result.setMsg(me.getValue(ResultMsgConstant.phoneFormatError));
            result.setStatus(ResultStruct.ERROR);
            return result;
        }
        return result;
    }


    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isEmpty(String arg) {
        return (arg == null) || (arg.trim().equals("") || "null".equals(arg));
    }

    public static boolean isEmail(String searchPhrase) {
        if (!isEmpty(searchPhrase)) {
            String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

            Pattern regex = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            Matcher matcher = regex.matcher(searchPhrase);
            return matcher.matches();
        }
        return false;
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^[1][0-9]{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean hasNullStr(String arg) {
        return (arg == null) || (arg.trim().equals("")) || (arg.trim().equalsIgnoreCase("null"));
    }

    public static boolean isDecimal(String str) {
        if ((str == null) || ("".equals(str)))
            return false;
        Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isNumber(String arg) {
        return (!isEmpty(arg)) && (arg.matches("-?[0-9]*"));
    }

    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static int GetRandomNumber6() {
        Random r = new Random();
        return r.nextInt(900000) + 100000;
    }

    public static int GetRandomNumber4() {
        Random r = new Random();
        return r.nextInt(9000) + 1000;
    }

    public static int getAge(String birthday) {
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(1);
            int age = Integer.valueOf(birthday.substring(0, 4)).intValue();
            return year - age + 1;
        } catch (Exception e) {
        }
        return 0;
    }

    public static void main(String[] iu) {
        System.out.println(isDecimal("93.2121212"));
        System.out.println(isDecimal("3"));
        System.out.println(isDecimal("0"));
        System.out.println(isDecimal("-13.2121212"));
    }

    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
            randGen = new Random();
        }
        if (numbersAndLetters == null) {
            numbersAndLetters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(25)];
        }
        return new String(randBuffer);
    }

    public static String fillNumWithLeft(String num, int length) {
        StringBuffer sb = new StringBuffer();
        if (!isEmpty(num)) {
            int i = getStrLength(num);
            if (i > length) {
                sb.append(num.substring(0, length));
            } else {
                for (; i < length; i++) {
                    sb.append("0");
                }
                sb.append(num);
            }
        } else {
            for (int i = 0; i < length; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    public static int getStrLength(String str) {
        if (!isEmpty(str)) {
            try {
                return str.getBytes("GBK").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static String getSpace(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
