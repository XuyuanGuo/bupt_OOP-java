public class Validator{
    /**
     * 判断输入字符串是否是合法的
     *
     * @param expression 输入字符串
     * @return  合法返回1，否则返回0
     */
    public static boolean isValidExpression(String expression){
        if(expression.equals(""))
            return false;
        Reader reader=new Reader(); //阅读器
        int index=0;                //当前下标
        int state=0;                  //使用有限状态自动机，0表示初态，1表示数字，2表示运算符，3表示'('，4表示')'
        int parenthesisCount=0;
        try{
            do{
                state=stateTransition(state,expression.charAt(index));
                switch(state) {
                    case 1:
                        index += reader.readNumber(expression, index).length();
                        break;
                    case 2:
                        index += reader.readOperator(expression, index).length();
                        break;
                    case 3:
                        index++;
                        parenthesisCount++;
                        break;
                    case 4:
                        index++;
                        parenthesisCount--;
                        if(parenthesisCount<0)
                            return false;
                        break;
                }
            }while(index<expression.length());
        }catch(IllegalArgumentException e){
            System.out.println(e);
            return false;
        }
        return parenthesisCount==0;
    }

    /**
     * 计算有限状态机的状态转移
     *
     * @param state 当前状态
     * @param input 当前输入
     * @return 下一状态
     */
    private static int stateTransition(int state,char input){
        switch (state) {
            case 1, 4:
                if (input == ')') {
                    state = 4;
                } else {
                    state = 2;
                }
                break;
            case 0, 2, 3:
                if (input == '(') {
                    state = 3;
                } else {
                    state = 1;
                }
                break;
        }
        return state;
    }
}