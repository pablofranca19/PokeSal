package utils;

import models.Pokemon;
import models.enums.EfeitoStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** CuraStatus com o Pokémon mockado: só interessa SE e COMO o item pede a cura. */
@ExtendWith(MockitoExtension.class)
class CuraStatusMockitoTest {

    @Mock
    private Pokemon pokemon;

    @Test
    @DisplayName("Cura quando o status do Pokémon é o que o item cura")
    void testCuraQuandoStatusCorresponde() {
        when(pokemon.getStatus()).thenReturn(EfeitoStatus.QUEIMADO);

        new CuraStatus("Antiqueimadura", EfeitoStatus.QUEIMADO).usar(pokemon);

        verify(pokemon).curarStatus();
    }

    @Test
    @DisplayName("Não cura quando o status é outro")
    void testNaoCuraQuandoStatusDiferente() {
        when(pokemon.getStatus()).thenReturn(EfeitoStatus.PARALISADO);

        new CuraStatus("Antiqueimadura", EfeitoStatus.QUEIMADO).usar(pokemon);

        verify(pokemon, never()).curarStatus();
    }

    @Test
    @DisplayName("Não cura Pokémon saudável")
    void testNaoCuraPokemonSaudavel() {
        when(pokemon.getStatus()).thenReturn(EfeitoStatus.NENHUM);

        new CuraStatus("Antídoto", EfeitoStatus.ENVENENADO).usar(pokemon);

        verify(pokemon, never()).curarStatus();
    }
}
