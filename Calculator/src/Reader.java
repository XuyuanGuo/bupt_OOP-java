public class Reader {
    /**
     * 在字符串的index位置读取一个数字
     *
     * @param expression 操作字符串
     * @param index 当前位置
     * @return 读取的数字
     * @throws IllegalArgumentException 读取内容不为数字
     */
    public String readNumber(String expression,int index) throws IllegalArgumentException{
        StringBuilder res=new StringBuilder();
        //使用有限状态自动机的方式，0为(-)(整数)，1为"(-)(整数).(小数)"
        int state=0;
        char input=expression.charAt(index);
        //考虑负数情况
        if(input=='-'){
            res.append(input);
            index++;
        }
        //处理数字部分
        while(index<expression.length()&&!isOperator(input=expression.charAt(index))&&!isParenthesis(input)){
            switch(state){
                case 0:
                    if(Character.isDigit(input)){
                        res.append(input);
                        state=0;
                    }else if(input=='.'){
                        res.append('.');
                        state=1;
                    }else{
                        throw new IllegalArgumentException("非法数字，位置："+index);
                    }
                    break;
                case 1:
                    if(Character.isDigit(input)){
                        res.append(input);
                        state=1;
                    }else{
                        throw new IllegalArgumentException("非法数字，位置："+index);
                    }
                    break;
            }
            index++;
        }
        if(res.toString().equals("")||res.toString().equals(".")||res.toString().equals("-"))
            throw new IllegalArgumentException("非法数字，位置："+index);
        return res.toString();
    }

    /**
     * 在字符串的index位置读取一个操作符
     *
     * @param expression 操作字符串
     * @param index 当前位置
     * @return 读取的操作符
     * @throws IllegalArgumentException 读取内容不为操作符
     */
    public String readOperator(String expression,int index) throws IllegalArgumentException{
        StringBuilder res=new StringBuilder();
        char input=expression.charAt(index);
        if(isOperator(input=expression.charAt(index))){
            res.append(input);
        }else{
            throw new IllegalArgumentException("非法操作符，位置："+index);
        }
        index++;
        return res.toString();
    };

    /**
     * 判断字符是否为运算符
     *
     * @param ch 字符
     * @return 是否为运算符
     */
    public boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    /**
     * 判断字符是否为括号
     *
     * @param ch 字符
     * @return 是否为括号
     */
    public boolean isParenthesis(char ch) {
        return ch == '(' || ch == ')';
    }
}
