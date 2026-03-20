package listtools;

public class DoubleStringListManager {

    private StringListManager managerListeA;
    private StringListManager managerListeB;

    private ComparatorListManager stringListComparator;
    private MixerListManager mixerListManager;

    public StringListManager setManagerA(StringListManager stringListManager) {
        this.managerListeA = stringListManager;
        return this.managerListeA;
    }

    public StringListManager setManagerB(StringListManager stringListManager) {
        this.managerListeB = stringListManager;
        return this.managerListeB;
    }

    public ComparatorListManager compare() {
        this.stringListComparator = new ComparatorListManager(this.managerListeA.getListe(), this.managerListeB.getListe());
        return this.stringListComparator;
    }

    public MixerListManager mix() {
        this.mixerListManager = new MixerListManager(this.managerListeA.getListe(), this.managerListeB.getListe());
        return this.mixerListManager;
    }

    public StringListManager getManagerA() {
        return managerListeA;
    }

    public StringListManager getManagerB() {
        return managerListeB;
    }

    public ComparatorListManager getComparator() {
        if (stringListComparator == null) {
            compare();
        }
        return stringListComparator;
    }

    public MixerListManager getMixerList() {
        if (mixerListManager == null) {
            mix();
        };
        return mixerListManager;
    }
}
