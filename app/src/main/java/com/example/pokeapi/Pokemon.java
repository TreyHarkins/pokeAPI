package com.example.pokeapi;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Pokemon {
    private int id;
    private String name;
    private int weight;
    private int height;

    @SerializedName("base_experience")
    private int baseExperience;

    private List<Ability> abilities;
    private List<Type> types;
    private List<Stat> stats;
    private List<Move> moves;

    public int getId() {
        return id;
    }
    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public int getBaseExperience() {
        return baseExperience;
    }
    public String getName() {
        return name;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public List<Type> getTypes() {
        return types;
    }

    public List<Stat> getStats() {
        return stats;
    }
    public List<Move> getMoves() {
        return moves;
    }

    public class Move {
        private MoveInfo move;

        public MoveInfo getMove() {
            return move;
        }
    }

    public class MoveInfo {
        private String name;

        public String getName() {
            return name;
        }
    }

    public class Ability {
        private AbilityInfo ability;

        public AbilityInfo getAbility() {
            return ability;
        }
    }

    public class AbilityInfo {
        private String name;

        public String getName() {
            return name;
        }
    }

    public class Type {
        private TypeInfo type;

        public TypeInfo getType() {
            return type;
        }
    }

    public class TypeInfo {
        private String name;

        public String getName() {
            return name;
        }
    }

    public class Stat {
        private int base_stat;
        private StatInfo stat;

        public int getBaseStat() {
            return base_stat;
        }

        public StatInfo getStat() {
            return stat;
        }
    }

    public class StatInfo {
        private String name;

        public String getName() {
            return name;
        }
    }

    private Sprites sprites;

    public Sprites getSprites() {
        return sprites;
    }

    public class Sprites {
        @SerializedName("front_default")
        private String frontDefault;

        public String getFrontDefault() {
            return frontDefault;
        }
    }
}
