#!/usr/bin/fish

for A in Amp Speaker; for B in Amp Speaker; for W in (seq 1 3); ./two.bash $A W $W $B > {$A}W{$W}{$B}.java; end; end; end
for A in Amp Speaker; for B in Amp Speaker; for M in (seq 1 5); ./two.bash $A M $M $B > {$A}M{$M}{$B}.java; end; end; end
