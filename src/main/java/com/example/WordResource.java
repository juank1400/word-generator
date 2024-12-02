package com.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.*;
import java.util.stream.Collectors;

@Path("/api/word")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WordResource {

    private static final String[] PHONEMES = {
            "a", "e", "i", "o", "u", "b", "c", "d", "f", "g", "h", 
            "j", "k", "l", "m", "n", "p", "r", "s", "t", "v", "w", 
            "x", "y", "z"
    };
    private static final Random RANDOM = new Random();

    @GET
    @Operation(
        summary = "Generar una palabra aleatoria",
        description = "Genera una palabra inexistente de longitud especificada utilizando fonemas.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Palabra generada", 
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = Map.class)))
        }
    )
    public Map<String, String> generateWord(@QueryParam("length") @DefaultValue("6") int length,
                                            @QueryParam("seed") String seed) {
        if (seed != null) {
            RANDOM.setSeed(seed.hashCode());
        }
        String word = RANDOM.ints(length, 0, PHONEMES.length)
                .mapToObj(i -> PHONEMES[i])
                .collect(Collectors.joining());
        return Map.of("word", word);
    }

    @GET
    @Path("/meaning")
    @Operation(
        summary = "Generar un significado ficticio",
        description = "Asigna un significado inventado a la palabra proporcionada.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Significado generado", 
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Palabra no proporcionada")
        }
    )
    public Map<String, String> generateMeaning(@QueryParam("word") String word) {
        if (word == null || word.isEmpty()) {
            throw new BadRequestException("La palabra no puede estar vacía.");
        }
        String[] meanings = {
                "Un objeto usado en rituales antiguos.",
                "Un sentimiento de alegría repentina.",
                "El sonido que hace el viento al cruzar una montaña.",
                "Una comida olvidada en los cuentos.",
                "Un estado mental de profunda concentración."
        };
        String meaning = meanings[RANDOM.nextInt(meanings.length)];
        return Map.of("word", word, "meaning", meaning);
    }

    @POST
    @Path("/custom")
    @Operation(
        summary = "Crear una palabra personalizada",
        description = "Crea una palabra única utilizando un prefijo y/o sufijo proporcionados.",
        requestBody = @RequestBody(description = "Prefijo y sufijo para la palabra",
                                   content = @Content(mediaType = "application/json",
                                   schema = @Schema(implementation = Map.class))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Palabra personalizada creada",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = Map.class)))
        }
    )
    public Map<String, String> createCustomWord(@QueryParam("prefix") String prefix, @QueryParam("suffix") String suffix) {
        String customWord = prefix + generateWord(4, null).get("word") + suffix;
        return Map.of("word", customWord);
    }
}
