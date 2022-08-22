//
//  OptionsViewController.swift
//  VolleyballPassingStats
//
//  Created by Cody King on 4/26/20.
//  Copyright © 2022 Cody J. King. All rights reserved.
//

import UIKit


class OptionsViewController: UIViewController {
    
    // MARK: Properties
    
    @IBOutlet weak var numPlayersTextField: UITextField!
    @IBOutlet weak var clearAllBtn: UIButton!
    @IBOutlet weak var numPlayersStepper: UIStepper!
    @IBOutlet weak var versionLabel: UILabel!
    @IBOutlet weak var exportCSVBtn: UIButton!
    @IBOutlet weak var exportTXTBtn: UIButton!
    
    
    static var clearAll = false
    static var numPlayers = 20
    static let MAX_NUM_PLAYERS = 20
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        // Set nav bar title
        self.title = "Options"
                
        numPlayersStepper.maximumValue = Double(OptionsViewController.MAX_NUM_PLAYERS)
        numPlayersStepper.minimumValue = 1
        numPlayersStepper.stepValue = 1
        numPlayersStepper.value = Double(OptionsViewController.MAX_NUM_PLAYERS)
        
        let appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String
        versionLabel.text = "Version: \(appVersion ?? "")"
    }
    
    // MARK: Actions
    
    @IBAction func changeNumPlayers(_ sender: UIStepper) {
        self.numPlayersTextField.text = Int(sender.value).description
        OptionsViewController.numPlayers = Int(sender.value)
    }

    @IBAction func clearAllData(_ sender: UIButton) {
        let refreshAlert = UIAlertController(title: "Refresh", message: "All data will be lost.", preferredStyle: UIAlertController.Style.alert)

        refreshAlert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
            OptionsViewController.clearAll = true
        }))

        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (action: UIAlertAction!) in
        }))

        present(refreshAlert, animated: true, completion: nil)
    }

    @IBAction func exportTXT(_ sender: Any) {
        askForFileNameAndExport(sender, "txt")
    }
    
    @IBAction func exportCSV(_ sender: Any) {
        askForFileNameAndExport(sender, "csv")
    }
    
    func askForFileNameAndExport(_ sender: Any, _ exportType: String) {
        // Set date for filename
        let dateFormatter = DateFormatter()
        let date = Date()
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = "yyyy-MM-dd--HH-mm-ss"
        let dateStr = dateFormatter.string(from: date)
        let fileNamePlaceholder = "stats--\(dateStr)";
        var inputText = "d"
        
        // Ask for file name
        let alertController = UIAlertController(title: "Export", message: "Enter name for the file", preferredStyle: .alert)
        alertController.addTextField { textField in
            textField.placeholder = fileNamePlaceholder
        }
        let confirmAction = UIAlertAction(title: "OK", style: .default) { [weak alertController] _ in
            guard let alertController = alertController, let inputTextField = alertController.textFields?.first else { return }
            
            inputText = inputTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines) ?? fileNamePlaceholder
            if(inputText.count == 0) {
                inputText = fileNamePlaceholder
            }
            
            if(exportType == "txt") {
                self.exportAsTXT(sender, inputText)
            }
            else if(exportType == "csv") {
                self.exportAsCSV(sender, inputText)
            }
        }
        alertController.addAction(confirmAction)
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        alertController.addAction(cancelAction)
        present(alertController, animated: true, completion: nil)
    }
    
    // Param: filename
    // Shares file as CSV (text, gmail, airdrop, etc.
    func exportAsCSV(_ sender: Any, _ fn: String) {
        
        // Create the CSV string
        var message = "\"Name\",\"Average\",\"No. of Passes\",\"Zeros\",\"Ones\",\"Twos\",\"Threes\",\"Total\"\n";

        for p in PlayerTableViewController.players {
            message += p.getStatsCSVText() + "\n";
        }
        
        let filename = getDocumentsDirectory().appendingPathComponent("\(fn).csv")

        do {
            try message.write(to: filename, atomically: true, encoding: String.Encoding.utf8)
        } catch {
            // failed to write file – bad permissions, bad filename, missing permissions, or more likely it can't be converted to the encoding
        }
        
        // Sets the title along with the URL
        let objectsToShare: [Any] = [filename]
        let activityVC = UIActivityViewController(activityItems: objectsToShare, applicationActivities: nil)
        activityVC.excludedActivityTypes = [UIActivity.ActivityType.addToReadingList]
        
        // add a sourceView to your popoverPresentationController (crash on iPad without this)
        activityVC.popoverPresentationController?.sourceView = sender as? UIView
        
        self.present(activityVC, animated: true, completion: nil)
    }
    
    func exportAsTXT(_ sender: Any, _ fn: String) {
        //Set the default sharing message.
        var message = "Here are your stats for the session:\n\n";
        message += "Group Average: ";
        message += "\(PlayerTableViewController.getGroupAvg()) \n\n";

        var i = 1
        for p in PlayerTableViewController.players {
            var name = p.name;

            if(name.count == 0) {
                name = "No Name (P\(i))";
            }

            message += name + "\n" + p.getStatsText() + "\n\n";
            
            i += 1
        }
        
        let filename = getDocumentsDirectory().appendingPathComponent("\(fn).txt")

        do {
            try message.write(to: filename, atomically: true, encoding: String.Encoding.utf8)
        } catch {
            // failed to write file – bad permissions, bad filename, missing permissions, or more likely it can't be converted to the encoding
        }
        
        // Sets the title along with the URL
        let objectsToShare: [Any] = [filename]
        let activityVC = UIActivityViewController(activityItems: objectsToShare, applicationActivities: nil)
        activityVC.excludedActivityTypes = [UIActivity.ActivityType.addToReadingList]
        
        // add a sourceView to your popoverPresentationController (crash on iPad without this)
        activityVC.popoverPresentationController?.sourceView = sender as? UIView
        
        self.present(activityVC, animated: true, completion: nil)
    }
    
    func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
    
    @IBAction func nameHyperlink(_ sender: UIButton) {
        if let url = NSURL(string: "https://codytheking.github.io/") {
            
            let refreshAlert = UIAlertController(title: "Open Link", message: "Go to developer's website?", preferredStyle: UIAlertController.Style.alert)

            refreshAlert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
                UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
            }))

            refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (action: UIAlertAction!) in
            }))

            present(refreshAlert, animated: true, completion: nil)
        }
    }
}
