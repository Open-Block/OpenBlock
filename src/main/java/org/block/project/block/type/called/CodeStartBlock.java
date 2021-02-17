package org.block.project.block.type.called;

import org.block.project.block.Block;
import org.block.project.block.group.BlockGroup;
import org.block.project.block.group.BlockSector;
import org.block.project.block.type.attachable.AttachableBlock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * If the code provided is a method, constructor
 */
public interface CodeStartBlock extends CalledBlock {

    @Override
    default String writeCode(int tab){
        return "";
    }

    default Set<Block> getChildren(Predicate<Block> predicate){
        if(!(this instanceof AttachableBlock)){
            return Collections.emptySet();
        }
        AttachableBlock target = (AttachableBlock)this;
        Set<AttachableBlock> toProcess = new HashSet<>();
        Set<Block> children = new HashSet<>();
        while(true){
            for(BlockGroup group : target.getGroups()){
                for (BlockSector<?> sector : group.getSectors()){
                    Optional<? extends Block> opBlock = sector.getAttachedBlock();
                    if(opBlock.isEmpty()){
                        continue;
                    }
                    if(predicate.test(opBlock.get())){
                        children.add(opBlock.get());
                    }
                    if(!(opBlock.get() instanceof AttachableBlock)){
                        continue;
                    }
                    toProcess.add((AttachableBlock) opBlock.get());
                }
            }
            if(toProcess.isEmpty()){
                break;
            }
            target = toProcess.stream().findAny().get();
            toProcess.remove(target);
        }
        return children;
    }
}