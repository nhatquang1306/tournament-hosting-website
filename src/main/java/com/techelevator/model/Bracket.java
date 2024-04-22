package com.techelevator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bracket {

    // the bracket is divided into upper and lower bracket
    private List<List<Match>> upper;
    private List<List<Match>> lower;
    public Bracket(List<Match> matchList) {
        upper = new ArrayList<>();
        lower = new ArrayList<>();
        for (int i = 0; i < matchList.size(); i++) {
            // if index is positive, it means the match is in the upper bracket, else it's in the lower bracket
            int index = matchList.get(i).getRound();
            if (index > 0) {
                if (upper.size() <= index - 1) upper.add(new ArrayList<>());
                upper.get(index - 1).add(matchList.get(i));
            } else {
                if (lower.size() <= index * (-1) - 1) lower.add(new ArrayList<>());
                lower.get(index * (-1) - 1).add(matchList.get(i));
            }
        }
    }

    // method to progress matches in a tournament
    public List<Match> progress(Match match) {
        List<Match> output = new ArrayList<>();
        int loserId = match.getSecondTeamId() == match.getWinnerId() ? match.getFirstTeamId() : match.getSecondTeamId();
        int index = match.getRound();
        // if index is the same as the upper bracket size, the match is the final match
        if (index == upper.size()) {
            return new ArrayList<>();
        }

        // if the match is in the upper bracket, send winner to next match and loser to lower bracket
        if (index > 0) {
            for (int i = 0; i < upper.get(index - 1).size(); i++) {
                if (upper.get(index - 1).get(i).getMatchId() == match.getMatchId()) {
                    if (i % 2 == 0) {
                        upper.get(index).get(i / 2).setFirstTeamId(match.getWinnerId());
                        if (index == 1) lower.get(0).get(i / 2).setFirstTeamId(loserId);
                    } else {
                        upper.get(index).get(i / 2).setSecondTeamId(match.getWinnerId());
                        if (index == 1) lower.get(0).get(i / 2).setSecondTeamId(loserId);
                    }
                    if (index >= 2 && index < upper.size()) {
                        lower.get(2 * index - 3).get(i).setFirstTeamId(loserId);
                        output.add(lower.get(2 * index - 3).get(i));
                    } else if (index == 1) {
                        output.add(lower.get(0).get(i / 2));
                    }
                    output.add(upper.get(index).get(i / 2));
                    break;
                }
            }
        // if the match is in lower bracket, send the winner to next match in the lower bracket and eliminate the loser
        } else {
            index *= -1;
            if (index == lower.size()) {
                upper.get(index - 1).get(0).setSecondTeamId(match.getWinnerId());
                output.add(upper.get(index - 1).get(0));
            } else {
                for (int i = 0; i < lower.get(index - 1).size(); i++) {
                    if (lower.get(index - 1).get(i).getMatchId() == match.getMatchId()) {
                        if (index % 2 == 0 && i % 2 == 0) {
                            lower.get(index).get(i / 2).setFirstTeamId(match.getWinnerId());
                        } else if (index % 2 == 0) {
                            lower.get(index).get(i / 2).setSecondTeamId(match.getWinnerId());
                        } else {
                            lower.get(index).get(i).setSecondTeamId(match.getWinnerId());
                        }
                        output.add(lower.get(index).get(index % 2 == 0 ? i / 2 : i));
                        break;
                    }
                }
            }
        }
        // return all newly generated matches
        return output;
    }
}
