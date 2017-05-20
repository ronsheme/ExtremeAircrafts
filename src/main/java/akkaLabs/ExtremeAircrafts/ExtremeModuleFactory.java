package akkaLabs.ExtremeAircrafts;

import com.google.inject.Module;
import org.testng.IModuleFactory;
import org.testng.ITestContext;

/**
 * Created by Ron on 20/05/2017.
 */
public class ExtremeModuleFactory implements IModuleFactory {
    @Override
    public Module createModule(ITestContext iTestContext, Class<?> aClass) {
        return new ExtremeModule();
    }
}
