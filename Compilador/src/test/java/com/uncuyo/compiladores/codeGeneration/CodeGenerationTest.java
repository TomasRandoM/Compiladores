package com.uncuyo.compiladores.codeGeneration;

import com.uncuyo.compiladores.Etapa5;
import com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree.AST;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class CodeGenerationTest {

    @Test
    void testGeneracionCodigoFailing() {
        Path base = Paths.get("tests/generacionDeCodigo/failing");
        try (Stream<Path> paths = Files.walk(base)) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".s"))
                    .forEach(path -> {
                        try {
                            System.out.println(path.toString());
                            AST.resetAST();
                            SymbolTable.resetSymbolTable();
                            Etapa5.execute(path.toString());
                        } catch (Exception e) {
                            fail("Falló el archivo: " + path + "\n" + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            fail("No se pudo abrir el directorio solicitado");
        }
    }

    @Test
    void testGeneracionCodigoPassing() {
        Path base = Paths.get("tests/generacionDeCodigo/passing");
        try (Stream<Path> paths = Files.walk(base)) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".s"))
                    .forEach(path -> {
                        try {
                            System.out.println(path.toString());
                            AST.resetAST();
                            SymbolTable.resetSymbolTable();
                            Etapa5.execute(path.toString());
                        } catch (Exception e) {
                            fail("Falló el archivo: " + path + "\n" + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            fail("No se pudo abrir el directorio solicitado");
        }
    }
}
