#!/bin/bash

note() {
	case $1 in
		M) echo 'FieldElements.points.kMidfieldNotes';;
		W) echo 'FieldElements.getFieldPoints().kWingNotes';;
	esac
}

cat <<EOF
package frc.robot.auto.routines.two;

import java.util.List;

import frc.robot.auto.actions.*;
import frc.robot.auto.routines.templates.MultiNoteAuto;
import frc.robot.game_elements.FieldElements;

public class $1$2$3$4 extends MultiNoteAuto {

    public $1$2$3$4() {
      super("$1$2$3$4"
      , List.of(Score$1::new, Score$4::new)
      , List.of(
          $(note $2).get($3-1)
        )
      );
    }
  
}
EOF
