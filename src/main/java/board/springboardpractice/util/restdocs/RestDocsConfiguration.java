//package board.springboardpractice.util.restdocs;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//
//@TestConfiguration
//public class RestDocsConfiguration {
//
//  @Bean
//  public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
//    return configurer ->
//            configurer
//                    .operationPreprocessors()
//                    .withRequestDefaults(prettyPrint())
//                    .withResponseDefaults(prettyPrint());
//  }
//}