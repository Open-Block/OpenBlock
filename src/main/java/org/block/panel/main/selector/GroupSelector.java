package org.block.panel.main.selector;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.*;
import java.util.stream.Collectors;

public class GroupSelector extends Parent implements Selector{

    private final String display;
    private final Set<String> searchTerms = new HashSet<>();

    public GroupSelector(String display, String... searchTerms){
        this.display = display;
        this.searchTerms.addAll(Arrays.asList(searchTerms));
    }

    public List<Selector> getSelectors(){
        return this.getChildrenUnmodifiable().stream()
                .filter(n -> n instanceof Selector)
                .map(n -> (Selector)n)
                .collect(Collectors.toList());
    }

    public void addChild(Node node){
        this.getChildren().add(node);
        this.getChildren().sort((o1, o2) -> {
            if(!(o1 instanceof Selector)){
                return 1;
            }
            if(!(o2 instanceof Selector)){
                return -1;
            }
            Selector selector1 = (Selector) o1;
            Selector selector2 = (Selector) o2;
            return selector1.getTitle().compareTo(selector2.getTitle());
        });
    }

    @Override
    @Deprecated
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override
    public Set<String> getSearchTerms() {
        Set<String> set = new HashSet<>(this.searchTerms);
        set.addAll(this.getSelectors().stream().map(Selector::getTitle).collect(Collectors.toList()));
        return set;
    }

    @Override
    public String getTitle() {
        return this.display;
    }
}
