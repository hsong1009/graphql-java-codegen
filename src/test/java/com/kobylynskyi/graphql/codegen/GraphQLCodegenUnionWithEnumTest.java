package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.kobylynskyi.graphql.codegen.TestUtils.assertSameTrimmedContent;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphQLCodegenUnionWithEnumTest {

    private final File outputBuildDir = new File("build/generated");
    private final File outputJavaClassesDir = new File("build/generated/com/kobylynskyi/graphql/enumunion");

    private GraphQLCodegen generator;

    @BeforeEach
    void init() {
        MappingConfig mappingConfig = new MappingConfig();
        mappingConfig.setPackageName("com.kobylynskyi.graphql.enumunion");
        List<String> schemas = Collections.singletonList(
                "src/test/resources/schemas/union-with-enum.graphqls"
        );
        generator = new GraphQLCodegen(schemas, outputBuildDir, mappingConfig);
    }

    @AfterEach
    void cleanup() {
        Utils.deleteDir(new File("build/generated"));
    }

    @Test
    void generate_CheckFiles() throws Exception {
        generator.generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());
        List<String> generatedFileNames = Arrays.stream(files).map(File::getName).sorted().collect(toList());
        assertEquals(Arrays.asList("EnumMember1.java", "EnumMember2.java", "EnumUnion.java"), generatedFileNames);

        for (File file : files) {
            assertSameTrimmedContent(
                    new File(String.format("src/test/resources/expected-classes/enum-union/%s.txt", file.getName())),
                    file);
        }
    }
}
