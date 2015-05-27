package parser;
import org.antlr.v4.runtime.misc.NotNull;


/**
 * Created by seanbaergen on 15-05-25.
 */
public class ExtendedVisitor extends InputParserBaseVisitor {

    @Override
    public Object visitUniform(@NotNull InputParserParser.UniformContext ctx) {
        double params[] = new double[2];
        params[0] = Double.parseDouble(ctx.getChild(0).getText());
        params[1] = Double.parseDouble(ctx.getChild(1).getText());
        return params;
    }

    @Override
    public Object visitExponential(@NotNull InputParserParser.ExponentialContext ctx) {
        return ctx.getChild(0);
    }

    @Override
    public Object visitGaussian(@NotNull InputParserParser.GaussianContext ctx) {
        double params[] = new double[3];
        params[0] = Double.parseDouble(ctx.getChild(0).getText());
        params[1] = Double.parseDouble(ctx.getChild(1).getText());
        params[2] = Double.parseDouble(ctx.getChild(2).getText());
        return params;
    }

    @Override
    public Object visitPoisson(@NotNull InputParserParser.PoissonContext ctx) {
        return ctx.getChild(0);
    }



}
