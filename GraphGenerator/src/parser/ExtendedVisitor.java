package parser;

import com.sun.istack.internal.NotNull;

/**
 * Created by seanbaergen on 15-05-25.
 */
public class ExtendedVisitor extends InputParserBaseVisitor {

    @Override
    public Object visitINTASS(@NotNull InputParserParser.INTASSContext ctx) {
        String option = ctx.getChild(0).getText();
        int option_new_value = Integer.parseInt(ctx.getChild(2).getText());
        System.out.println("Setting < " +option + " > to int: " + option_new_value);
        return null;
    }

    @Override
    public Object visitSTRASS(@NotNull InputParserParser.STRASSContext ctx) {
        String option = ctx.getChild(0).getText();
        String option_new_value = ctx.getChild(2).getText();
        System.out.println("Setting < " +option+ " > to string: " + option_new_value);
        return null;
    }


}
