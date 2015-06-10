import org.antlr.v4.runtime.misc.NotNull;

import java.lang.reflect.Type;


/**
 * Created by seanbaergen on 15-05-25.
 */
public class ExtendedVisitor extends InputParserBaseVisitor<ValueNew> {

    @Override
    public ValueNew visitUniform(@NotNull InputParserParser.UniformContext ctx) {
        double params[] = new double[2];
        params[0] = Double.parseDouble(ctx.getChild(0).getText());
        params[1] = Double.parseDouble(ctx.getChild(1).getText());
        System.out.println(params[0] + " " + params[1]);
        return null;
    }

    @Override
    public ValueNew visitExponential(@NotNull InputParserParser.ExponentialContext ctx) {
        System.out.println("HELLO");
        return null;
    }

    @Override
    public ValueNew visitGaussian(@NotNull InputParserParser.GaussianContext ctx) {
        double params[] = new double[3];
        params[0] = Double.parseDouble(ctx.getChild(0).getText());
        this.visit(ctx.getChild(0));
        params[1] = Double.parseDouble(ctx.getChild(1).getText());
        params[2] = Double.parseDouble(ctx.getChild(2).getText());
        System.out.println("HELLO");
        return null;
    }

    @Override
    public ValueNew visitPoisson(@NotNull InputParserParser.PoissonContext ctx) {
        System.out.println("HELLO");
        return null;
    }

    @Override
    public ValueNew visitBeginning(@NotNull InputParserParser.BeginningContext ctx) {
        System.out.println("HELLO");
        for(int c = 0;c < ctx.getChildCount(); ++c) {
            System.out.println("HELLO");
            this.visit(ctx.getChild(c));
        }
        return null;
    }


}
