package app.entity;
public enum Status {
    Open , Accepted, Assessment, In_progress, Technical_review, AIR, Resolved, Rejected, Closed, Reopen, Move;

    public static Status getByOrder(int n){
        switch(n){
            case 1: return Status.Open;
            case 2: return Status.Accepted;
            case 3: return Status.In_progress;
            case 4: return Status.Technical_review;
            case 5: return Status.AIR;
            case 6: return Status.Resolved;
            case 7: return Status.Rejected;
            case 8: return Status.Closed;
            case 9: return Status.Reopen;
            case 10: return Status.Move;
            case 11: return Status.Assessment;
            default: return Status.Open;
        }

    }

    public static Status getByString(String str){
        switch(str){
            case "Open": return Status.Open;
            case "Accepted": return Status.Accepted;
            case "In_progress": return Status.In_progress;
            case "Technical_review": return Status.Technical_review;
            case "AIR": return Status.AIR;
            case "Resolved": return Status.Resolved;
            case "Rejected": return Status.Rejected;
            case "Closed": return Status.Closed;
            case "Reopen": return Status.Reopen;
            case "Move": return Status.Move;
            case "Assessment": return Status.Assessment;
            default: throw new IllegalArgumentException("Wrong Status value");
        }
    }



}
