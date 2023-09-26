import java.util.Stack;

public class Calculator {
    /**
     * 计算表达式的结果
     *
     * @param expression 要计算的表达式
     * @return 计算结果
     * @throws ArithmeticException 如果出现除数为零的错误
     */
    public static double evaluateExpression(String expression) throws ArithmeticException {
        // 使用两个栈分别存储操作数和运算符
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        Reader reader=new Reader(); //阅读器
        int index=0;                //当前下标
        int state=0;                  //使用有限状态自动机，0表示初态，1表示数字，2表示运算符，3表示'('，4表示')'
        String item;
        do {
            state=stateTransition(state, expression.charAt(index));
            switch (state) {
                // 如果是数字，则将其作为操作数压入操作数栈
                case 1:
                    item=reader.readNumber(expression,index);
                    operandStack.push(Double.parseDouble(item));
                    index+=item.length();
                    break;
                // 如果是运算符
                case 2:
                    item=reader.readOperator(expression,index);
                    while (!operatorStack.isEmpty() &&
                            !isOpeningParenthesis(operatorStack.peek()) &&
                            hasHigherOrEqualPrecedence(operatorStack.peek(),item.charAt(0))) {
                        processOperator(operandStack, operatorStack);
                    }
                    operatorStack.push(item.charAt(0));
                    index+=item.length();
                    break;
                //如果是左括号，则直接压入运算符栈
                case 3:
                    operatorStack.push('(');
                    index++;
                    break;
                //如果是右括号，则处理与之匹配的左括号之间的运算符
                case 4:
                    while (!operatorStack.isEmpty() && !isOpeningParenthesis(operatorStack.peek())) {
                        processOperator(operandStack, operatorStack);
                    }
                    // 弹出左括号
                    if (!operatorStack.isEmpty() && isOpeningParenthesis(operatorStack.peek())) {
                        operatorStack.pop();
                    }
                    index++;
            }
        } while (index < expression.length());

        // 处理剩余的运算符
        while (!operatorStack.isEmpty()) {
            processOperator(operandStack, operatorStack);
        }

        // 返回最终计算结果
        return operandStack.pop();
    }

    /**
     * 处理运算符
     *
     * @param operandStack   操作数栈
     * @param operatorStack  运算符栈
     * @throws ArithmeticException 如果出现除数为零的错误
     */
    private static void processOperator(Stack<Double> operandStack, Stack<Character> operatorStack) throws ArithmeticException {
        char operator = operatorStack.pop();
        double operand2 = operandStack.pop();
        double operand1 = operandStack.pop();

        switch (operator) {
            case '+':
                operandStack.push(operand1 + operand2);
                break;
            case '-':
                operandStack.push(operand1 - operand2);
                break;
            case '*':
                operandStack.push(operand1 * operand2);
                break;
            case '/':
                if (operand2 == 0) {
                    throw new ArithmeticException("除数不能为零");
                }
                operandStack.push(operand1 / operand2);
                break;
        }
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

    /**
     * 判断字符是否为开括号
     *
     * @param ch 字符
     * @return 是否为开括号
     */
    private static boolean isOpeningParenthesis(char ch) {
        return ch == '(';
    }

    /**
     * 判断第一个运算符的优先级是否大于等于第二个运算符的优先级
     *
     * @param operator1 第一个运算符
     * @param operator2 第二个运算符
     * @return 是否大于等于
     */
    private static boolean hasHigherOrEqualPrecedence(char operator1, char operator2) {
        int precedence1 = getOperatorPrecedence(operator1);
        int precedence2 = getOperatorPrecedence(operator2);
        return precedence1 >= precedence2;
    }

    /**
     * 获取运算符的优先级
     *
     * @param operator 运算符
     * @return 优先级
     */
    private static int getOperatorPrecedence(char operator) {
        if (operator == '*' || operator == '/') {
            return 2;
        } else if (operator == '+' || operator == '-') {
            return 1;
        } else {
            return 0; // 其他运算符（如括号）的优先级设为最低
        }
    }
}
